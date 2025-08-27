package com.algotutor.messenger.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class SendMessageRequest {

	@NotBlank(message = "Sender is required")
	@Size(min = 3, max = 50, message = "Sender name must be between 1 and 50 characters")
	private String sender;
	
	@NotBlank(message = "Content is required")
    @Size(min = 1, max = 1000, message = "Message content must be between 1 and 1000 characters")
	private String content;
	
	
	public SendMessageRequest() {}

    public SendMessageRequest(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }
}
