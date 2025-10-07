package com.inghubs.brokage.exception;

public class InvalidOrderStatusException extends RuntimeException {

    public InvalidOrderStatusException() {
        super("Only pending status orders can be cancelled");
    }

}
