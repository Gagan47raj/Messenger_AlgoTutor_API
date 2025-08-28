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
	
	@Indexed
    private String userId;
	
	

	private String sender;
	private String content;
	
	// New fields for media support
    private MessageType messageType = MessageType.TEXT;
    private String mediaUrl;
    private String mediaFileName;
    private Long mediaFileSize;
    private String mimeType;

	@CreatedDate
	private LocalDateTime timestamp;

	public Message(String roomId, String userId, String sender, String content) {
        this.roomId = roomId;
        this.userId = userId;
        this.sender = sender;
        this.content = content;
        this.messageType = MessageType.TEXT;
        this.timestamp = LocalDateTime.now();
    }
	
	
	// New constructor for media messages
    public Message(String roomId, String userId, String sender, String content, 
                   MessageType messageType, String mediaUrl, String mediaFileName, 
                   Long mediaFileSize, String mimeType) {
        this.roomId = roomId;
        this.userId = userId;
        this.sender = sender;
        this.content = content;
        this.messageType = messageType;
        this.mediaUrl = mediaUrl;
        this.mediaFileName = mediaFileName;
        this.mediaFileSize = mediaFileSize;
        this.mimeType = mimeType;
        this.timestamp = LocalDateTime.now();
    }
    
    
    public enum MessageType {
        TEXT, IMAGE, VIDEO, AUDIO, DOCUMENT
    }

}
