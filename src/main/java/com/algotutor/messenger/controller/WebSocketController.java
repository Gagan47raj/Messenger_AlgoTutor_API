package com.algotutor.messenger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.algotutor.messenger.dto.SendMessageRequest;
import com.algotutor.messenger.service.ChatService;

@Controller
public class WebSocketController {

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat/{roomId}")
    public void sendMessage(@DestinationVariable String roomId, 
                           SendMessageRequest request,
                           SimpMessageHeaderAccessor headerAccessor) {
        
        // Get username from WebSocket session attributes
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        
        if (username == null) {
            // Fallback: try to get from authentication
            if (headerAccessor.getUser() != null) {
                username = headerAccessor.getUser().getName();
            } else {
                throw new RuntimeException("User not authenticated in WebSocket");
            }
        }
        
        chatService.sendMessageViaWebSocket(roomId, request, username);
    }
    
    @MessageMapping("/private/{chatId}")
    public void sendPrivateMessage(@DestinationVariable String chatId,
                                  SendMessageRequest request,
                                  SimpMessageHeaderAccessor headerAccessor) {
        
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        
        if (username == null && headerAccessor.getUser() != null) {
            username = headerAccessor.getUser().getName();
        }
        
        if (username == null) {
            throw new RuntimeException("User not authenticated in WebSocket");
        }
        
        chatService.sendPrivateMessageViaWebSocket(chatId, request, username);
    }

}