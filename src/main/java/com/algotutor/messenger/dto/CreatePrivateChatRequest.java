package com.algotutor.messenger.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreatePrivateChatRequest {
	@NotBlank(message = "Target username is required")
    private String targetUsername;
    
    public CreatePrivateChatRequest() {}
    
    public CreatePrivateChatRequest(String targetUsername) {
        this.targetUsername = targetUsername;
    }
}
