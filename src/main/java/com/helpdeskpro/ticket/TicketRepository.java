package com.helpdeskpro.ticket;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, String> {
    long countByStatusNot(TicketStatus status);
    long countByPriorityInAndStatusNot(List<String> priorities, TicketStatus status);
}
