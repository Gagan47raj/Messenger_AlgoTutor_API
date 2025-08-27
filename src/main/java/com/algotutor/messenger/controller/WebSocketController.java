package com.algotutor.messenger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import com.algotutor.messenger.dto.SendMessageRequest;
import com.algotutor.messenger.service.ChatService;

@Controller
public class WebSocketController {

	@Autowired
	private ChatService chatService;

	@MessageMapping("/chat/{roomId}")
	public void sendMessage(@DestinationVariable String roomId, @Payload SendMessageRequest request) {
		chatService.sendMessage(roomId, request);
	}
}
