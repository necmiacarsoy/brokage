package com.inghubs.brokage.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class BrokageAsset {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long customerId;
    private String assetName;
    private double size;
    private double usableSize;
    
    public BrokageAsset() {
    }

    public BrokageAsset(Long customerId, String assetName, double size, double usableSize) {
        this.customerId = customerId;
        this.assetName = assetName;
        this.size = size;
        this.usableSize = usableSize;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getUsableSize() {
        return usableSize;
    }

    public void setUsableSize(double usableSize) {
        this.usableSize = usableSize;
    }

    

    
    
}
