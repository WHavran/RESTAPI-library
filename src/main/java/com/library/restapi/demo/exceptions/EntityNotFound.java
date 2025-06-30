package com.library.restapi.demo.exceptions;

public class EntityNotFound extends RuntimeException {
    public EntityNotFound(String message) {
        super(message);
    }

  public EntityNotFound() {
      super("Entity was not found");
  }
}
