package com.insiders.poc1.exception;


public class AddressLimitExceededException extends RuntimeException{
    public AddressLimitExceededException(String message) {
        super(message);
    }
}
