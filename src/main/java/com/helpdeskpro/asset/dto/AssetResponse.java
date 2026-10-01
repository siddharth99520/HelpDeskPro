package com.helpdeskpro.asset.dto;

public class AssetResponse {
    private String id;
    private String name;
    private String status;
    private String assignedToId;

    public AssetResponse(String id, String name, String status, String assignedToId) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.assignedToId = assignedToId;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getStatus() { return status; }
    public String getAssignedToId() { return assignedToId; }
}
