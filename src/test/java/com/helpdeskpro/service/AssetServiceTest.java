package com.helpdeskpro.service;

import com.helpdeskpro.domain.Asset;
import com.helpdeskpro.domain.AssetStatus;
import com.helpdeskpro.domain.Role;
import com.helpdeskpro.domain.User;
import com.helpdeskpro.exception.AssetAlreadyAssignedException;
import com.helpdeskpro.exception.InvalidTransitionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AssetServiceTest {

    private AssetService assetService;
    private User employee;
    private User engineer;
    private Asset asset;

    @BeforeEach
    void setUp() {
        assetService = new AssetService();
        employee = new User(UUID.randomUUID().toString(), "Alice", Role.EMPLOYEE);
        engineer = new User(UUID.randomUUID().toString(), "Bob", Role.ENGINEER);
        asset = new Asset(UUID.randomUUID().toString(), "Dell Monitor");
        assetService.addAsset(asset);
    }

    @Test
    void testAssignAsset() {
        assetService.assign(asset.getId(), employee);
        assertEquals(AssetStatus.ASSIGNED, asset.getStatus());
        assertEquals(employee, asset.getAssignedTo());
    }

    @Test
    void testDoubleAssignmentThrowsException() {
        assetService.assign(asset.getId(), employee);
        
        assertThrows(AssetAlreadyAssignedException.class, () -> {
            assetService.assign(asset.getId(), engineer);
        });
        
        // Should still be assigned to first employee
        assertEquals(employee, asset.getAssignedTo());
    }

    @Test
    void testReturnAsset() {
        assetService.assign(asset.getId(), employee);
        assetService.returnAsset(asset.getId());
        
        assertEquals(AssetStatus.AVAILABLE, asset.getStatus());
        assertNull(asset.getAssignedTo());
    }

    @Test
    void testInvalidTransition() {
        assetService.retireAsset(asset.getId());
        
        // Cannot assign a retired asset
        assertThrows(InvalidTransitionException.class, () -> {
            assetService.assign(asset.getId(), employee);
        });
    }
}
