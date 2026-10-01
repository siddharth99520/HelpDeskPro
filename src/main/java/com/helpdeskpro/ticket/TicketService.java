package com.helpdeskpro.ticket;

import com.helpdeskpro.user.User;
import com.helpdeskpro.shared.Category;
import com.helpdeskpro.shared.exception.InvalidTransitionException;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TicketService {
    private final Map<String, Ticket> ticketStore = new ConcurrentHashMap<>();
    
    private static final EnumMap<TicketStatus, Set<TicketStatus>> VALID_TRANSITIONS = new EnumMap<>(TicketStatus.class);
    
    static {
        VALID_TRANSITIONS.put(TicketStatus.OPEN, Set.of(TicketStatus.ASSIGNED));
        VALID_TRANSITIONS.put(TicketStatus.ASSIGNED, Set.of(TicketStatus.IN_PROGRESS));
        VALID_TRANSITIONS.put(TicketStatus.IN_PROGRESS, Set.of(TicketStatus.RESOLVED, TicketStatus.WAITING_FOR_USER));
        VALID_TRANSITIONS.put(TicketStatus.WAITING_FOR_USER, Set.of(TicketStatus.IN_PROGRESS));
        VALID_TRANSITIONS.put(TicketStatus.RESOLVED, Set.of(TicketStatus.CLOSED, TicketStatus.IN_PROGRESS));
        VALID_TRANSITIONS.put(TicketStatus.CLOSED, Set.of());
    }

    public Ticket createTicket(String title, String description, User createdBy, Category category) {
        Ticket ticket = new Ticket(UUID.randomUUID().toString(), title, description, createdBy, category);
        ticketStore.put(ticket.getId(), ticket);
        return ticket;
    }

    public Ticket assignTicket(String ticketId, User assignee) {
        Ticket ticket = getTicket(ticketId);
        updateStatus(ticket, TicketStatus.ASSIGNED);
        ticket.setAssignedTo(assignee);
        return ticket;
    }

    public void updateStatus(Ticket ticket, TicketStatus newStatus) {
        Set<TicketStatus> allowedStates = VALID_TRANSITIONS.get(ticket.getStatus());
        if (allowedStates == null || !allowedStates.contains(newStatus)) {
            throw new InvalidTransitionException("Cannot transition ticket from " + ticket.getStatus() + " to " + newStatus);
        }
        ticket.setStatus(newStatus);
    }
    
    public Ticket getTicket(String id) {
        Ticket ticket = ticketStore.get(id);
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket not found");
        }
        return ticket;
    }
}
