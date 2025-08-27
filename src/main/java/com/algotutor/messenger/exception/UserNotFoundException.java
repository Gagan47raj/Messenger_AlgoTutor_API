package com.algotutor.messenger.exception;

public class UserNotFoundException extends ChatException {
    public UserNotFoundException(String message) {
        super(message);
    }
}