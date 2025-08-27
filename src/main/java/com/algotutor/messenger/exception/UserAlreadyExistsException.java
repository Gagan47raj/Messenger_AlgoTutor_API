package com.algotutor.messenger.exception;

public class UserAlreadyExistsException extends ChatException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
