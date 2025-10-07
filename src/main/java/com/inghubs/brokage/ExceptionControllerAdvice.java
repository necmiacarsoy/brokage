package com.inghubs.brokage;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.inghubs.brokage.exception.AssetNotFoundException;
import com.inghubs.brokage.exception.InvalidOrderStatusException;
import com.inghubs.brokage.exception.InvalidUserException;
import com.inghubs.brokage.exception.NotAuthToDeleteException;
import com.inghubs.brokage.exception.NotEnoughAssetException;
import com.inghubs.brokage.exception.NotEnoughTRYAssetException;
import com.inghubs.brokage.exception.OrderNotFoundException;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<String> handleInvalidUserException(InvalidUserException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AssetNotFoundException.class)
    public ResponseEntity<String> handleAssetNotFoundException(AssetNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidOrderStatusException.class)
    public ResponseEntity<String> handleInvalidOrderStatusException(InvalidOrderStatusException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotAuthToDeleteException.class)
    public ResponseEntity<String> handleNotAuthToDeleteException(NotAuthToDeleteException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotEnoughAssetException.class)
    public ResponseEntity<String> handleNotEnoughAssetException(NotEnoughAssetException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotEnoughTRYAssetException.class)
    public ResponseEntity<String> handleNotEnoughTRYAssetException(NotEnoughTRYAssetException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleOrderNotFoundException(OrderNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

}
