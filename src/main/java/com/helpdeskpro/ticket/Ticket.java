package com.helpdeskpro.ticket;

import com.helpdeskpro.user.User;
import com.helpdeskpro.shared.Category;
import com.helpdeskpro.asset.Asset;

import java.time.Instant;

public class Ticket {
    private String id;
    private String title;
    private String description;
    private TicketStatus status;
    private User createdBy;
    private User assignedTo;
    private Category category;
    private Asset asset;
    private Instant createdAt;

    public Ticket(String id, String title, String description, User createdBy, Category category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
        this.category = category;
        this.status = TicketStatus.OPEN;
        this.createdAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Category getCategory() {
        return category;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
