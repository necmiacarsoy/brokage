package com.inghubs.brokage.exception;

public class InvalidUserException extends RuntimeException {

    public InvalidUserException() {
        super("Only admin account can create users");
    }

}
