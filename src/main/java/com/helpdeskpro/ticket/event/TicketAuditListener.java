package com.helpdeskpro.ticket.event;

import com.helpdeskpro.ticket.TicketAudit;
import com.helpdeskpro.ticket.TicketAuditRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class TicketAuditListener {
    private final TicketAuditRepository auditRepository;

    public TicketAuditListener(TicketAuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public void handleTicketStatusChangedEvent(TicketStatusChangedEvent event) {
        TicketAudit audit = new TicketAudit(
                UUID.randomUUID().toString(),
                event.getTicket(),
                event.getOldStatus() != null ? event.getOldStatus().name() : null,
                event.getNewStatus().name(),
                event.getChangedByUserId()
        );
        auditRepository.save(audit);
    }
}
