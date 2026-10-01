package com.helpdeskpro.ticket;

import com.helpdeskpro.shared.Category;
import com.helpdeskpro.user.Role;
import com.helpdeskpro.user.User;
import com.helpdeskpro.shared.exception.InvalidTransitionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TicketServiceTest {

    private TicketService ticketService;
    private User employee;
    private User engineer;
    private Category category;

    @BeforeEach
    void setUp() {
        ticketService = new TicketService();
        employee = new User(UUID.randomUUID().toString(), "Alice Employee", Role.EMPLOYEE);
        engineer = new User(UUID.randomUUID().toString(), "Bob Engineer", Role.ENGINEER);
        category = new Category(UUID.randomUUID().toString(), "Software");
    }

    @Test
    void testCreateTicket() {
        Ticket ticket = ticketService.createTicket("Title", "Desc", employee, category);
        assertNotNull(ticket.getId());
        assertEquals(TicketStatus.OPEN, ticket.getStatus());
        assertEquals(employee, ticket.getCreatedBy());
    }

    @Test
    void testValidTransitions() {
        Ticket ticket = ticketService.createTicket("Title", "Desc", employee, category);
        
        // OPEN -> ASSIGNED
        ticketService.assignTicket(ticket.getId(), engineer);
        assertEquals(TicketStatus.ASSIGNED, ticket.getStatus());
        
        // ASSIGNED -> IN_PROGRESS
        ticketService.updateStatus(ticket, TicketStatus.IN_PROGRESS);
        assertEquals(TicketStatus.IN_PROGRESS, ticket.getStatus());
        
        // IN_PROGRESS -> WAITING_FOR_USER
        ticketService.updateStatus(ticket, TicketStatus.WAITING_FOR_USER);
        assertEquals(TicketStatus.WAITING_FOR_USER, ticket.getStatus());
        
        // WAITING_FOR_USER -> IN_PROGRESS
        ticketService.updateStatus(ticket, TicketStatus.IN_PROGRESS);
        assertEquals(TicketStatus.IN_PROGRESS, ticket.getStatus());
        
        // IN_PROGRESS -> RESOLVED
        ticketService.updateStatus(ticket, TicketStatus.RESOLVED);
        assertEquals(TicketStatus.RESOLVED, ticket.getStatus());
        
        // RESOLVED -> IN_PROGRESS (Reopen)
        ticketService.updateStatus(ticket, TicketStatus.IN_PROGRESS);
        assertEquals(TicketStatus.IN_PROGRESS, ticket.getStatus());
        
        // IN_PROGRESS -> RESOLVED
        ticketService.updateStatus(ticket, TicketStatus.RESOLVED);
        
        // RESOLVED -> CLOSED
        ticketService.updateStatus(ticket, TicketStatus.CLOSED);
        assertEquals(TicketStatus.CLOSED, ticket.getStatus());
    }

    @Test
    void testInvalidTransition() {
        Ticket ticket = ticketService.createTicket("Title", "Desc", employee, category);
        
        assertThrows(InvalidTransitionException.class, () -> {
            ticketService.updateStatus(ticket, TicketStatus.IN_PROGRESS);
        });
        
        assertEquals(TicketStatus.OPEN, ticket.getStatus());
    }
}
