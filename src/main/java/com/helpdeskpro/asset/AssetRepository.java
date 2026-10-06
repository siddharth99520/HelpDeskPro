package com.helpdeskpro.asset;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, String> {
    long countByStatus(AssetStatus status);
}
