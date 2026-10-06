package com.helpdeskpro.asset;

import com.helpdeskpro.asset.dto.AssetAssignRequest;
import com.helpdeskpro.asset.dto.AssetRequest;
import com.helpdeskpro.asset.dto.AssetResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/assets")
@PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')") // Only Managers and Admins manage assets
public class AssetController {

    private final AssetService assetService;
    private final AssetAssignmentRepository assignmentRepository;

    public AssetController(AssetService assetService, AssetAssignmentRepository assignmentRepository) {
        this.assetService = assetService;
        this.assignmentRepository = assignmentRepository;
    }

    @PostMapping
    public AssetResponse createAsset(@Valid @RequestBody AssetRequest request) {
        Asset asset = new Asset(UUID.randomUUID().toString(), request.getName());
        assetService.addAsset(asset);
        return new AssetResponse(asset.getId(), asset.getName(), asset.getStatus().name(), null);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()") // Anyone authenticated can view an asset
    public AssetResponse getAsset(@PathVariable String id) {
        Asset asset = assetService.getAsset(id);
        String assignedTo = assignmentRepository.findByAssetIdAndReturnedAtIsNull(id)
                .map(assignment -> assignment.getUser().getId())
                .orElse(null);
        return new AssetResponse(asset.getId(), asset.getName(), asset.getStatus().name(), assignedTo);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()") // Anyone authenticated can view assets list
    public java.util.List<AssetResponse> getAllAssets() {
        return assetService.getAllAssets().stream()
                .map(asset -> {
                    String assignedTo = assignmentRepository.findByAssetIdAndReturnedAtIsNull(asset.getId())
                        .map(assignment -> assignment.getUser().getId())
                        .orElse(null);
                    return new AssetResponse(asset.getId(), asset.getName(), asset.getStatus().name(), assignedTo);
                })
                .collect(java.util.stream.Collectors.toList());
    }

    @PutMapping("/{id}/assign")
    public void assignAsset(@PathVariable String id, @Valid @RequestBody AssetAssignRequest request) {
        assetService.assign(id, request.getUserId());
    }

    @PutMapping("/{id}/return")
    public void returnAsset(@PathVariable String id) {
        assetService.returnAsset(id);
    }

    @PutMapping("/{id}/repair")
    public void repairAsset(@PathVariable String id) {
        assetService.repairAsset(id);
    }

    @PutMapping("/{id}/retire")
    public void retireAsset(@PathVariable String id) {
        assetService.retireAsset(id);
    }
}
