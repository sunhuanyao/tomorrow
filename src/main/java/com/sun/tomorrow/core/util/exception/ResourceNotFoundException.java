package com.sun.tomorrow.core.util.exception;

public class ResourceNotFoundException extends RuntimeException {


    public ResourceNotFoundException() {
        super("resource not found");
    }

    public ResourceNotFoundException(String path) {
        super("resource not found: " + path);
    }
}
