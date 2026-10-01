package com.helpdeskpro.ticket;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "ticket_audits")
public class TicketAudit {
    @Id
    private String id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;
    
    private String oldStatus;
    private String newStatus;
    private String changedBy;
    private Instant changedAt;

    public TicketAudit() {}

    public TicketAudit(String id, Ticket ticket, String oldStatus, String newStatus, String changedBy) {
        this.id = id;
        this.ticket = ticket;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changedBy = changedBy;
        this.changedAt = Instant.now();
    }

    public String getId() { return id; }
    public Ticket getTicket() { return ticket; }
    public String getOldStatus() { return oldStatus; }
    public String getNewStatus() { return newStatus; }
    public String getChangedBy() { return changedBy; }
    public Instant getChangedAt() { return changedAt; }
}
