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

    @GetMapping
    public java.util.List<TicketResponse> getAllTickets() {
        return ticketService.getAllTickets().stream()
                .map(ticket -> new TicketResponse(ticket.getId(), ticket.getTitle(), ticket.getDescription(), ticket.getStatus().name()))
                .collect(java.util.stream.Collectors.toList());
    }

    @GetMapping("/{id}")
    public TicketResponse getTicket(@PathVariable String id) {
        Ticket ticket = ticketService.getTicket(id);
        return new TicketResponse(ticket.getId(), ticket.getTitle(), ticket.getDescription(), ticket.getStatus().name());
    }

    @PutMapping("/{id}/assign")
    public TicketResponse assignTicket(@PathVariable String id, @Valid @RequestBody com.helpdeskpro.ticket.dto.TicketAssignRequest request) {
        Ticket ticket = ticketService.assignTicket(id, request.getAssigneeId());
        return new TicketResponse(ticket.getId(), ticket.getTitle(), ticket.getDescription(), ticket.getStatus().name());
    }

    @PutMapping("/{id}/status")
    public TicketResponse updateStatus(@PathVariable String id, @Valid @RequestBody com.helpdeskpro.ticket.dto.TicketStatusRequest request) {
        Ticket ticket = ticketService.getTicket(id);
        ticketService.updateStatus(ticket, TicketStatus.valueOf(request.getStatus()));
        return new TicketResponse(ticket.getId(), ticket.getTitle(), ticket.getDescription(), ticket.getStatus().name());
    }
}
