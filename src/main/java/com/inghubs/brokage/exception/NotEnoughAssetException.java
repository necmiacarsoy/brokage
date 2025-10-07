package com.inghubs.brokage.exception;

public class NotEnoughAssetException extends RuntimeException {

    public NotEnoughAssetException(String assetName) {
        super("Not enough " + assetName + " asset");
    }

}
