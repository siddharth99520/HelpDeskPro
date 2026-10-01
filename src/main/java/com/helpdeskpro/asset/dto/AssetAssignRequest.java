package com.helpdeskpro.asset.dto;

import javax.validation.constraints.NotBlank;

public class AssetAssignRequest {
    @NotBlank(message = "User ID is required")
    private String userId;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}
