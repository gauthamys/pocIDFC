package com.example.standalone.exceptions;

public class AccountDoesNotExistError extends Exception {
    public AccountDoesNotExistError(String message){
        super(message);
    }
}
