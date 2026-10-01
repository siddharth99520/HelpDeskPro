package com.helpdeskpro.ticket.event;

import com.helpdeskpro.ticket.Ticket;
import com.helpdeskpro.ticket.TicketStatus;
import org.springframework.context.ApplicationEvent;

public class TicketStatusChangedEvent extends ApplicationEvent {
    private final Ticket ticket;
    private final TicketStatus oldStatus;
    private final TicketStatus newStatus;
    private final String changedByUserId;

    public TicketStatusChangedEvent(Object source, Ticket ticket, TicketStatus oldStatus, TicketStatus newStatus, String changedByUserId) {
        super(source);
        this.ticket = ticket;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changedByUserId = changedByUserId;
    }

    public Ticket getTicket() { return ticket; }
    public TicketStatus getOldStatus() { return oldStatus; }
    public TicketStatus getNewStatus() { return newStatus; }
    public String getChangedByUserId() { return changedByUserId; }
}
