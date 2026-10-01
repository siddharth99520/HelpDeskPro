package com.helpdeskpro.asset;

import com.helpdeskpro.user.User;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "asset_assignments")
public class AssetAssignment {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id")
    private Asset asset;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "assigned_at")
    private Instant assignedAt;

    @Column(name = "returned_at")
    private Instant returnedAt;

    protected AssetAssignment() {}

    public AssetAssignment(String id, Asset asset, User user) {
        this.id = id;
        this.asset = asset;
        this.user = user;
        this.assignedAt = Instant.now();
    }

    public String getId() { return id; }
    public Asset getAsset() { return asset; }
    public User getUser() { return user; }
    public Instant getAssignedAt() { return assignedAt; }
    public Instant getReturnedAt() { return returnedAt; }
    public void setReturnedAt(Instant returnedAt) { this.returnedAt = returnedAt; }
}
