package com.helpdeskpro.asset;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AssetAssignmentRepository extends JpaRepository<AssetAssignment, String> {
    Optional<AssetAssignment> findByAssetIdAndReturnedAtIsNull(String assetId);
}
