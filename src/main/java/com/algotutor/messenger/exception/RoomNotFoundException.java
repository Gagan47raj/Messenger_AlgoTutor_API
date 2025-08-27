package com.algotutor.messenger.exception;


public class RoomNotFoundException extends ChatException {
    public RoomNotFoundException(String roomId) {
        super("Room not found with id: " + roomId);
    }
}