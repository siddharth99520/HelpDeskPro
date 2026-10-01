package com.helpdeskpro.ticket;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketAuditRepository extends JpaRepository<TicketAudit, String> {
}
