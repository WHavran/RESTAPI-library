package com.library.restapi.demo.exceptions;

public class EntityDeletionNotAllowedException extends RuntimeException {
    public EntityDeletionNotAllowedException(String message) {
        super(message);
    }
}
