package com.example.antLogging.exceptions;

public class AccountDoesNotExistError extends Exception {
    public AccountDoesNotExistError(String message){
        super(message);
    }
}
