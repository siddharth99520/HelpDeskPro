package com.helpdeskpro.ticket.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TicketRequest {
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotBlank(message = "Priority is required")
    private String priority;
    
    @NotNull(message = "Category ID is required")
    private String categoryId;
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    
    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }
}
