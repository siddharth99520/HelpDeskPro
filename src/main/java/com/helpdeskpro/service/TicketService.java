package com.helpdeskpro.service;

import com.helpdeskpro.domain.Ticket;
import com.helpdeskpro.domain.TicketStatus;
import com.helpdeskpro.domain.User;
import com.helpdeskpro.domain.Category;
import com.helpdeskpro.exception.InvalidTransitionException;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TicketService {
    private final Map<String, Ticket> ticketStore = new ConcurrentHashMap<>();
    
    private static final EnumMap<TicketStatus, Set<TicketStatus>> VALID_TRANSITIONS = new EnumMap<>(TicketStatus.class);
    
    static {
        // OPEN -> ASSIGNED
        VALID_TRANSITIONS.put(TicketStatus.OPEN, Set.of(TicketStatus.ASSIGNED));
        // ASSIGNED -> IN_PROGRESS
        VALID_TRANSITIONS.put(TicketStatus.ASSIGNED, Set.of(TicketStatus.IN_PROGRESS));
        // IN_PROGRESS -> RESOLVED, WAITING_FOR_USER
        VALID_TRANSITIONS.put(TicketStatus.IN_PROGRESS, Set.of(TicketStatus.RESOLVED, TicketStatus.WAITING_FOR_USER));
        // WAITING_FOR_USER -> IN_PROGRESS
        VALID_TRANSITIONS.put(TicketStatus.WAITING_FOR_USER, Set.of(TicketStatus.IN_PROGRESS));
        // RESOLVED -> CLOSED, IN_PROGRESS (reopen)
        VALID_TRANSITIONS.put(TicketStatus.RESOLVED, Set.of(TicketStatus.CLOSED, TicketStatus.IN_PROGRESS));
        // CLOSED -> none
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
