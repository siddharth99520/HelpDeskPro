package com.helpdeskpro.exception;

public class AssetAlreadyAssignedException extends RuntimeException {
    public AssetAlreadyAssignedException(String message) {
        super(message);
    }
}
