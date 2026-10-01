package com.helpdeskpro.ticket;

import com.helpdeskpro.ticket.dto.TicketRequest;
import com.helpdeskpro.ticket.dto.TicketResponse;
import com.helpdeskpro.user.User;
import com.helpdeskpro.user.Role;
import com.helpdeskpro.shared.Category;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public TicketResponse createTicket(@RequestBody TicketRequest request) {
        // Hard-coded dependencies for Phase 3 before persistence layer
        User user = new User(UUID.randomUUID().toString(), "Mock User", Role.EMPLOYEE);
        Category category = new Category(UUID.randomUUID().toString(), "Mock Category");
        
        Ticket ticket = ticketService.createTicket(request.getTitle(), request.getDescription(), user, category);
        return new TicketResponse(ticket.getId(), ticket.getTitle(), ticket.getDescription(), ticket.getStatus().name());
    }

    @GetMapping("/{id}")
    public TicketResponse getTicket(@PathVariable String id) {
        Ticket ticket = ticketService.getTicket(id);
        return new TicketResponse(ticket.getId(), ticket.getTitle(), ticket.getDescription(), ticket.getStatus().name());
    }
}
