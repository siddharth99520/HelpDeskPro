package com.helpdeskpro.asset;

import com.helpdeskpro.user.User;
import com.helpdeskpro.user.UserRepository;
import com.helpdeskpro.shared.exception.InvalidTransitionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.EnumMap;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@SuppressWarnings("null")
public class AssetService {
    private final AssetRepository assetRepository;
    private final AssetAssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    
    private static final EnumMap<AssetStatus, Set<AssetStatus>> VALID_TRANSITIONS = new EnumMap<>(AssetStatus.class);

    static {
        VALID_TRANSITIONS.put(AssetStatus.AVAILABLE, Set.of(AssetStatus.ASSIGNED, AssetStatus.IN_REPAIR, AssetStatus.RETIRED));
        VALID_TRANSITIONS.put(AssetStatus.ASSIGNED, Set.of(AssetStatus.AVAILABLE, AssetStatus.IN_REPAIR, AssetStatus.RETIRED));
        VALID_TRANSITIONS.put(AssetStatus.IN_REPAIR, Set.of(AssetStatus.AVAILABLE, AssetStatus.RETIRED));
        VALID_TRANSITIONS.put(AssetStatus.RETIRED, Set.of());
    }

    public AssetService(AssetRepository assetRepository, AssetAssignmentRepository assignmentRepository, UserRepository userRepository) {
        this.assetRepository = assetRepository;
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;
    }

    public Asset addAsset(Asset asset) {
        return assetRepository.save(asset);
    }

    public Asset getAsset(String id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asset not found"));
    }

    public void assign(String assetId, String userId) {
        Asset asset = getAsset(assetId);
        
        if (asset.getStatus() == AssetStatus.ASSIGNED) {
            Optional<AssetAssignment> currentAssignment = assignmentRepository.findByAssetIdAndReturnedAtIsNull(assetId);
            String assigneeName = currentAssignment.map(a -> a.getUser().getName()).orElse("Unknown");
            throw new AssetAlreadyAssignedException("Asset is already assigned to " + assigneeName);
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
                
        updateStatus(asset, AssetStatus.ASSIGNED);
        assetRepository.save(asset);
        
        AssetAssignment assignment = new AssetAssignment(UUID.randomUUID().toString(), asset, user);
        assignmentRepository.save(assignment);
    }

    public void returnAsset(String assetId) {
        Asset asset = getAsset(assetId);
        
        updateStatus(asset, AssetStatus.AVAILABLE);
        assetRepository.save(asset);
        
        assignmentRepository.findByAssetIdAndReturnedAtIsNull(assetId).ifPresent(assignment -> {
            assignment.setReturnedAt(Instant.now());
            assignmentRepository.save(assignment);
        });
    }

    public void repairAsset(String assetId) {
        Asset asset = getAsset(assetId);
        updateStatus(asset, AssetStatus.IN_REPAIR);
        assetRepository.save(asset);
        
        assignmentRepository.findByAssetIdAndReturnedAtIsNull(assetId).ifPresent(assignment -> {
            assignment.setReturnedAt(Instant.now());
            assignmentRepository.save(assignment);
        });
    }
    
    public void retireAsset(String assetId) {
        Asset asset = getAsset(assetId);
        updateStatus(asset, AssetStatus.RETIRED);
        assetRepository.save(asset);
        
        assignmentRepository.findByAssetIdAndReturnedAtIsNull(assetId).ifPresent(assignment -> {
            assignment.setReturnedAt(Instant.now());
            assignmentRepository.save(assignment);
        });
    }

    private void updateStatus(Asset asset, AssetStatus newStatus) {
        Set<AssetStatus> allowedStates = VALID_TRANSITIONS.get(asset.getStatus());
        if (allowedStates == null || !allowedStates.contains(newStatus)) {
            throw new InvalidTransitionException("Cannot transition asset from " + asset.getStatus() + " to " + newStatus);
        }
        asset.setStatus(newStatus);
    }
}
