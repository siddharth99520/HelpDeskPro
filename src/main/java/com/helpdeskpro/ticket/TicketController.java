package com.helpdeskpro.ticket;

import com.helpdeskpro.ticket.dto.TicketRequest;
import com.helpdeskpro.ticket.dto.TicketResponse;
import com.helpdeskpro.security.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public TicketResponse createTicket(@Valid @RequestBody TicketRequest request, @AuthenticationPrincipal UserPrincipal principal) {
        Ticket ticket = ticketService.createTicket(request.getTitle(), request.getDescription(), principal.getId(), request.getCategoryId(), request.getPriority());
        return new TicketResponse(ticket.getId(), ticket.getTitle(), ticket.getDescription(), ticket.getStatus().name());
    }

    @GetMapping("/{id}")
    public TicketResponse getTicket(@PathVariable String id) {
        Ticket ticket = ticketService.getTicket(id);
        return new TicketResponse(ticket.getId(), ticket.getTitle(), ticket.getDescription(), ticket.getStatus().name());
    }
}
