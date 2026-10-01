package com.helpdeskpro.ticket.dto;

import javax.validation.constraints.NotBlank;

public class TicketAssignRequest {
    @NotBlank(message = "Assignee ID is required")
    private String assigneeId;

    public String getAssigneeId() { return assigneeId; }
    public void setAssigneeId(String assigneeId) { this.assigneeId = assigneeId; }
}
