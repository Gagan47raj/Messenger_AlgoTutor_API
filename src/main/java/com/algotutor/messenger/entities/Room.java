package com.algotutor.messenger.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "rooms")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {

	@Id
	private String id;
	
	@Indexed(unique = true)
	private String roomId;
	
	private String name;
	
	@CreatedDate
    private LocalDateTime createdAt;
	
	
    private List<String> messageIds;
    
    
    public Room(String name, String roomId) {
        this.name = name;
        this.roomId = roomId;
        this.createdAt = LocalDateTime.now();
        this.messageIds = new ArrayList<>();
    }

}
