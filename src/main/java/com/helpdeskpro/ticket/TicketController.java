package com.helpdeskpro.ticket;

import com.helpdeskpro.ticket.dto.TicketRequest;
import com.helpdeskpro.ticket.dto.TicketResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public TicketResponse createTicket(@Valid @RequestBody TicketRequest request) {
        // hard-coded user for now until Phase 5 (Security Auth context)
        String mockUserId = "u1"; 
        
        Ticket ticket = ticketService.createTicket(request.getTitle(), request.getDescription(), mockUserId, request.getCategoryId(), request.getPriority());
        return new TicketResponse(ticket.getId(), ticket.getTitle(), ticket.getDescription(), ticket.getStatus().name());
    }

    @GetMapping("/{id}")
    public TicketResponse getTicket(@PathVariable String id) {
        Ticket ticket = ticketService.getTicket(id);
        return new TicketResponse(ticket.getId(), ticket.getTitle(), ticket.getDescription(), ticket.getStatus().name());
    }
}
