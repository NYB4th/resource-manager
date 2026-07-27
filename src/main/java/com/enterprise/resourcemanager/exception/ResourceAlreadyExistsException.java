package com.enterprise.resourcemanager.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String message)
    {
        super(message);
    }

}
