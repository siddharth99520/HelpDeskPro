package com.helpdeskpro.ticket;

import com.helpdeskpro.shared.Category;
import com.helpdeskpro.shared.CategoryRepository;
import com.helpdeskpro.user.Role;
import com.helpdeskpro.user.User;
import com.helpdeskpro.user.UserRepository;
import com.helpdeskpro.shared.exception.InvalidTransitionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SuppressWarnings("null")
class TicketServiceTest {

    private TicketService ticketService;
    private TicketRepository ticketRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private org.springframework.context.ApplicationEventPublisher eventPublisher;
    
    private User employee;
    private User engineer;
    private Category category;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        ticketRepository = Mockito.mock(TicketRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        categoryRepository = Mockito.mock(CategoryRepository.class);
        eventPublisher = Mockito.mock(org.springframework.context.ApplicationEventPublisher.class);
        
        ticketService = new TicketService(ticketRepository, userRepository, categoryRepository, eventPublisher);
        
        employee = new User("u1", "Alice Employee", Role.EMPLOYEE);
        engineer = new User("u2", "Bob Engineer", Role.ENGINEER);
        category = new Category("c1", "Software");
        
        ticket = new Ticket("t1", "Title", "Desc", employee, category, "NORMAL");
        
        when(userRepository.findById("u1")).thenReturn(Optional.of(employee));
        when(userRepository.findById("u2")).thenReturn(Optional.of(engineer));
        when(categoryRepository.findById("c1")).thenReturn(Optional.of(category));
        when(ticketRepository.findById("t1")).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(i -> i.getArguments()[0]);
    }

    @Test
    void testCreateTicket() {
        Ticket result = ticketService.createTicket("Title", "Desc", "u1", "c1", "NORMAL");
        assertNotNull(result.getId());
        assertEquals(TicketStatus.OPEN, result.getStatus());
        assertEquals(employee, result.getCreatedBy());
    }

    @Test
    void testValidTransitions() {
        ticketService.assignTicket("t1", "u2");
        assertEquals(TicketStatus.ASSIGNED, ticket.getStatus());
        
        ticketService.updateStatus(ticket, TicketStatus.IN_PROGRESS);
        assertEquals(TicketStatus.IN_PROGRESS, ticket.getStatus());
        
        ticketService.updateStatus(ticket, TicketStatus.RESOLVED);
        assertEquals(TicketStatus.RESOLVED, ticket.getStatus());
        
        ticketService.updateStatus(ticket, TicketStatus.CLOSED);
        assertEquals(TicketStatus.CLOSED, ticket.getStatus());
    }

    @Test
    void testInvalidTransition() {
        assertThrows(InvalidTransitionException.class, () -> {
            ticketService.updateStatus(ticket, TicketStatus.IN_PROGRESS);
        });
        
        assertEquals(TicketStatus.OPEN, ticket.getStatus());
    }
}
