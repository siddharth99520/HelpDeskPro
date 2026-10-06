package com.helpdeskpro.ticket;

import com.helpdeskpro.shared.Category;
import com.helpdeskpro.shared.CategoryRepository;
import com.helpdeskpro.user.User;
import com.helpdeskpro.user.UserRepository;
import com.helpdeskpro.shared.exception.InvalidTransitionException;
import com.helpdeskpro.security.UserPrincipal;
import com.helpdeskpro.ticket.event.TicketStatusChangedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ApplicationEventPublisher eventPublisher;
    
    private static final EnumMap<TicketStatus, Set<TicketStatus>> VALID_TRANSITIONS = new EnumMap<>(TicketStatus.class);
    
    static {
        VALID_TRANSITIONS.put(TicketStatus.OPEN, Set.of(TicketStatus.ASSIGNED));
        VALID_TRANSITIONS.put(TicketStatus.ASSIGNED, Set.of(TicketStatus.IN_PROGRESS));
        VALID_TRANSITIONS.put(TicketStatus.IN_PROGRESS, Set.of(TicketStatus.RESOLVED, TicketStatus.WAITING_FOR_USER));
        VALID_TRANSITIONS.put(TicketStatus.WAITING_FOR_USER, Set.of(TicketStatus.IN_PROGRESS));
        VALID_TRANSITIONS.put(TicketStatus.RESOLVED, Set.of(TicketStatus.CLOSED, TicketStatus.IN_PROGRESS));
        VALID_TRANSITIONS.put(TicketStatus.CLOSED, Set.of());
    }

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository, CategoryRepository categoryRepository, ApplicationEventPublisher eventPublisher) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.eventPublisher = eventPublisher;
    }

    public Ticket createTicket(String title, String description, String createdById, String categoryId, String priority) {
        User createdBy = userRepository.findById(createdById)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
                
        Ticket ticket = new Ticket(UUID.randomUUID().toString(), title, description, createdBy, category, priority);
        Ticket saved = ticketRepository.save(ticket);
        eventPublisher.publishEvent(new com.helpdeskpro.ticket.event.TicketStatusChangedEvent(this, saved, null, TicketStatus.OPEN, createdById));
        return saved;
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public Ticket assignTicket(String ticketId, String assigneeId) {
        Ticket ticket = getTicket(ticketId);
        User assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
                
        updateStatus(ticket, TicketStatus.ASSIGNED);
        ticket.setAssignedTo(assignee);
        return ticket;
    }

    public void updateStatus(Ticket ticket, TicketStatus newStatus) {
        Set<TicketStatus> allowedStates = VALID_TRANSITIONS.get(ticket.getStatus());
        if (allowedStates == null || !allowedStates.contains(newStatus)) {
            throw new InvalidTransitionException("Cannot transition ticket from " + ticket.getStatus() + " to " + newStatus);
        }
        TicketStatus oldStatus = ticket.getStatus();
        ticket.setStatus(newStatus);
        ticketRepository.save(ticket);
        
        String currentUserId = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserPrincipal) {
            currentUserId = ((UserPrincipal) principal).getId();
        }
        eventPublisher.publishEvent(new TicketStatusChangedEvent(this, ticket, oldStatus, newStatus, currentUserId));
    }
    
    @PreAuthorize("hasAnyRole('ENGINEER', 'MANAGER', 'ADMIN') or @securityService.isTicketOwner(#id, principal.id)")
    public Ticket getTicket(String id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));
    }

    public java.util.List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
}
