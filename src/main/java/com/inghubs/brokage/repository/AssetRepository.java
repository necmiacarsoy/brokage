package com.inghubs.brokage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.inghubs.brokage.entity.BrokageAsset;

public interface AssetRepository extends JpaRepository<BrokageAsset, Long> {

    List<BrokageAsset> findByCustomerIdAndAssetName(@Param("customerId") long customerId, 
        @Param("assetName") String assetName);
    List<BrokageAsset> findByCustomerId(@Param("customerId") long customerId);

}
