package com.inghubs.brokage.exception;

public class AssetNotFoundException extends RuntimeException {

    public AssetNotFoundException(String assetName) {
        super("Asset not found " + assetName);
    }

}
