package com.helpdeskpro.security;

import com.helpdeskpro.ticket.TicketRepository;
import org.springframework.stereotype.Service;

@Service("securityService")
public class SecurityService {

    private final TicketRepository ticketRepository;

    public SecurityService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public boolean isTicketOwner(String ticketId, String userId) {
        return ticketRepository.findById(ticketId)
                .map(ticket -> ticket.getCreatedBy().getId().equals(userId))
                .orElse(false);
    }
}
