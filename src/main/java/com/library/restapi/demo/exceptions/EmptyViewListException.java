package com.library.restapi.demo.exceptions;

public class EmptyViewListException extends RuntimeException {
    public EmptyViewListException(String message) {
        super(message);
    }

    public EmptyViewListException() {
        super("Called list is empty or doesn't exist");
    }
}
