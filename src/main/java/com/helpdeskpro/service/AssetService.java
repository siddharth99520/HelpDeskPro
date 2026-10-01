package com.helpdeskpro.service;

import com.helpdeskpro.domain.Asset;
import com.helpdeskpro.domain.AssetStatus;
import com.helpdeskpro.domain.User;
import com.helpdeskpro.exception.AssetAlreadyAssignedException;
import com.helpdeskpro.exception.InvalidTransitionException;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AssetService {
    private final Map<String, Asset> assetStore = new ConcurrentHashMap<>();
    
    private static final EnumMap<AssetStatus, Set<AssetStatus>> VALID_TRANSITIONS = new EnumMap<>(AssetStatus.class);

    static {
        VALID_TRANSITIONS.put(AssetStatus.AVAILABLE, Set.of(AssetStatus.ASSIGNED, AssetStatus.IN_REPAIR, AssetStatus.RETIRED));
        VALID_TRANSITIONS.put(AssetStatus.ASSIGNED, Set.of(AssetStatus.AVAILABLE, AssetStatus.IN_REPAIR, AssetStatus.RETIRED));
        VALID_TRANSITIONS.put(AssetStatus.IN_REPAIR, Set.of(AssetStatus.AVAILABLE, AssetStatus.RETIRED));
        VALID_TRANSITIONS.put(AssetStatus.RETIRED, Set.of());
    }

    public void addAsset(Asset asset) {
        assetStore.put(asset.getId(), asset);
    }

    public Asset getAsset(String id) {
        Asset asset = assetStore.get(id);
        if (asset == null) {
            throw new IllegalArgumentException("Asset not found");
        }
        return asset;
    }

    public void assign(String assetId, User user) {
        Asset asset = getAsset(assetId);
        
        if (asset.getStatus() == AssetStatus.ASSIGNED) {
            throw new AssetAlreadyAssignedException("Asset is already assigned to " + asset.getAssignedTo().name());
        }
        
        updateStatus(asset, AssetStatus.ASSIGNED);
        asset.setAssignedTo(user);
    }

    public void returnAsset(String assetId) {
        Asset asset = getAsset(assetId);
        
        updateStatus(asset, AssetStatus.AVAILABLE);
        asset.setAssignedTo(null);
    }

    public void repairAsset(String assetId) {
        Asset asset = getAsset(assetId);
        updateStatus(asset, AssetStatus.IN_REPAIR);
        asset.setAssignedTo(null);
    }
    
    public void retireAsset(String assetId) {
        Asset asset = getAsset(assetId);
        updateStatus(asset, AssetStatus.RETIRED);
        asset.setAssignedTo(null);
    }

    private void updateStatus(Asset asset, AssetStatus newStatus) {
        Set<AssetStatus> allowedStates = VALID_TRANSITIONS.get(asset.getStatus());
        if (allowedStates == null || !allowedStates.contains(newStatus)) {
            throw new InvalidTransitionException("Cannot transition asset from " + asset.getStatus() + " to " + newStatus);
        }
        asset.setStatus(newStatus);
    }
}
