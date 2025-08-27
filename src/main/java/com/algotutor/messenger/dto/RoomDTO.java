package com.algotutor.messenger.dto;

import java.time.LocalDateTime;

import com.algotutor.messenger.entities.Room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO {

	    private String id;
	    private String name;
	    private String roomId;
	    private LocalDateTime createdAt;
	    private int messageCount;
	    
	    public RoomDTO(Room room) {
	        this.id = room.getId();
	        this.roomId = room.getRoomId();
	        this.name = room.getName();
	        this.createdAt = room.getCreatedAt();
	        this.messageCount = room.getMessageIds() != null ? room.getMessageIds().size() : 0;
	    }

}
