package com.algotutor.messenger.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "messages")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Message {

	@Id
	private String id;
	
	@Indexed
	private String roomId;
	
	 private String sender;
	 private String content;
	 
	 @CreatedDate
	 private LocalDateTime timestamp;
	 
	 public Message(String roomId, String sender, String content) {
	        this.roomId = roomId;
	        this.sender = sender;
	        this.content = content;
	        this.timestamp = LocalDateTime.now();
	    }

}
