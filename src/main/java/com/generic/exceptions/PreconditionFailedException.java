package com.generic.exceptions;

public class PreconditionFailedException extends RuntimeException {
    public PreconditionFailedException(String message) {
        super(message);
    }
}
