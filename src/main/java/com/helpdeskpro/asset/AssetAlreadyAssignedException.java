package com.helpdeskpro.asset;

public class AssetAlreadyAssignedException extends RuntimeException {
    public AssetAlreadyAssignedException(String message) {
        super(message);
    }
}
