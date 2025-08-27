package com.algotutor.messenger.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateRoomRequest {

	@NotBlank(message = "Room name is required")
	@Size(min = 3, max = 100, message = "Room name must be between 1 and 100 characters")
	private String name;
	
	@NotBlank(message = "Room ID is required")
	private String roomId;
	
	public CreateRoomRequest() {}
	
	
	public CreateRoomRequest(String name, String roomId) {
        this.name = name;
        this.roomId = roomId;
    }
	
	
}
