package com.library.restapi.demo.exceptions;

public class ObjectIsNull extends RuntimeException {
    public ObjectIsNull(String message) {
        super(message);
    }

    public ObjectIsNull() {
        super("Object/input is can't be null");
    }
}
