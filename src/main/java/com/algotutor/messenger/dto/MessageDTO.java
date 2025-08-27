package com.algotutor.messenger.dto;

import java.time.LocalDateTime;

import com.algotutor.messenger.entities.Message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageDTO {

	    private String id;
	    private String roomId;
	    private String userId;
	    private String sender;
	    private String content;
	    private LocalDateTime timestamp;
	    
	    public MessageDTO(Message message) {
	        this.id = message.getId();
	        this.roomId = message.getRoomId();
	        this.userId = message.getUserId();
	        this.sender = message.getSender();
	        this.content = message.getContent();
	        this.timestamp = message.getTimestamp();
	    }

}
