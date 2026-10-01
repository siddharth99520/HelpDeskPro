package com.helpdeskpro.ticket.dto;

import javax.validation.constraints.NotBlank;

public class TicketStatusRequest {
    @NotBlank(message = "Status is required")
    private String status;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
