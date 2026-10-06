package com.helpdeskpro.ticket.dto;

import java.time.Instant;

public class TicketResponse {
    private String id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private String createdBy;
    private String assignedTo;
    private Instant createdAt;

    public TicketResponse(String id, String title, String description, String status, String priority, String createdBy, String assignedTo, Instant createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.createdBy = createdBy;
        this.assignedTo = assignedTo;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public String getPriority() { return priority; }
    public String getCreatedBy() { return createdBy; }
    public String getAssignedTo() { return assignedTo; }
    public Instant getCreatedAt() { return createdAt; }
}
