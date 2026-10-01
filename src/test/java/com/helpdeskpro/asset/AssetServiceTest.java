package com.helpdeskpro.asset;

import com.helpdeskpro.user.Role;
import com.helpdeskpro.user.User;
import com.helpdeskpro.user.UserRepository;
import com.helpdeskpro.shared.exception.InvalidTransitionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AssetServiceTest {

    private AssetService assetService;
    private AssetRepository assetRepository;
    private AssetAssignmentRepository assignmentRepository;
    private UserRepository userRepository;
    
    private User employee;
    private User engineer;
    private Asset asset;

    @BeforeEach
    void setUp() {
        assetRepository = Mockito.mock(AssetRepository.class);
        assignmentRepository = Mockito.mock(AssetAssignmentRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        
        assetService = new AssetService(assetRepository, assignmentRepository, userRepository);
        
        employee = new User("u1", "Alice", Role.EMPLOYEE);
        engineer = new User("u2", "Bob", Role.ENGINEER);
        asset = new Asset("a1", "Dell Monitor");
        
        when(userRepository.findById("u1")).thenReturn(Optional.of(employee));
        when(userRepository.findById("u2")).thenReturn(Optional.of(engineer));
        when(assetRepository.findById("a1")).thenReturn(Optional.of(asset));
        when(assignmentRepository.findByAssetIdAndReturnedAtIsNull("a1")).thenReturn(Optional.empty());
    }

    @Test
    void testAssignAsset() {
        assetService.assign("a1", "u1");
        assertEquals(AssetStatus.ASSIGNED, asset.getStatus());
    }

    @Test
    void testDoubleAssignmentThrowsException() {
        asset.setStatus(AssetStatus.ASSIGNED);
        AssetAssignment assignment = new AssetAssignment("asg1", asset, employee);
        when(assignmentRepository.findByAssetIdAndReturnedAtIsNull("a1")).thenReturn(Optional.of(assignment));
        
        assertThrows(AssetAlreadyAssignedException.class, () -> {
            assetService.assign("a1", "u2");
        });
    }

    @Test
    void testReturnAsset() {
        asset.setStatus(AssetStatus.ASSIGNED);
        AssetAssignment assignment = new AssetAssignment("asg1", asset, employee);
        when(assignmentRepository.findByAssetIdAndReturnedAtIsNull("a1")).thenReturn(Optional.of(assignment));
        
        assetService.returnAsset("a1");
        
        assertEquals(AssetStatus.AVAILABLE, asset.getStatus());
        assertNotNull(assignment.getReturnedAt());
    }

    @Test
    void testInvalidTransition() {
        asset.setStatus(AssetStatus.RETIRED);
        
        assertThrows(InvalidTransitionException.class, () -> {
            assetService.assign("a1", "u1");
        });
    }
}
