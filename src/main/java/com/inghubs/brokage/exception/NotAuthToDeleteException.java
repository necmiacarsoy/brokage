package com.inghubs.brokage.exception;

public class NotAuthToDeleteException extends RuntimeException {

    public NotAuthToDeleteException() {
        super("Not authorized to delete the order");
    }
}
