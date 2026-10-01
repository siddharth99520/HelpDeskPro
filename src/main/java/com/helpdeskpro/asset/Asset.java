package com.helpdeskpro.asset;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "assets")
public class Asset {
    @Id
    private String id;
    
    private String name;
    
    @Enumerated(EnumType.STRING)
    private AssetStatus status;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    @Version
    private Integer version;

    protected Asset() {}

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
    
    public Integer getVersion() {
        return version;
    }
}
