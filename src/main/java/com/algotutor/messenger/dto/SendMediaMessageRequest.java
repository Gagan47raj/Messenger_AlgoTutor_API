package com.algotutor.messenger.dto;

import com.algotutor.messenger.entities.Message.MessageType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SendMediaMessageRequest {
    private String content; // Optional caption for media
    
    @NotNull(message = "Message type is required")
    private MessageType messageType;
    
    @NotNull(message = "Media URL is required")
    private String mediaUrl;
    
    private String mediaFileName;
    private Long mediaFileSize;
    private String mimeType;
    
    public SendMediaMessageRequest() {}
    
    public SendMediaMessageRequest(String content, MessageType messageType, String mediaUrl) {
        this.content = content;
        this.messageType = messageType;
        this.mediaUrl = mediaUrl;
    }
}
