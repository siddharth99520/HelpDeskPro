package com.helpdeskpro.ticket.dto;

public class TicketResponse {
    private String id;
    private String title;
    private String description;
    private String status;

    public TicketResponse(String id, String title, String description, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
}
