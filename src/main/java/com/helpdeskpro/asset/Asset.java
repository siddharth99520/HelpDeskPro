package com.helpdeskpro.asset;

import com.helpdeskpro.user.User;

public class Asset {
    private String id;
    private String name;
    private AssetStatus status;
    private User assignedTo;

    public Asset(String id, String name) {
        this.id = id;
        this.name = name;
        this.status = AssetStatus.AVAILABLE;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AssetStatus getStatus() {
        return status;
    }

    public void setStatus(AssetStatus status) {
        this.status = status;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }
}
