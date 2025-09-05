package com.algotutor.messenger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.algotutor.messenger.dto.MessageDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RoomMessageSubscriber {

	@Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void receiveMessage(String message) {
        try {
            MessageDTO messageDto = objectMapper.readValue(message, MessageDTO.class);
            System.out.println("Received room message from Redis: " + message);
            
            // Broadcast to WebSocket subscribers
            messagingTemplate.convertAndSend("/topic/rooms/" + messageDto.getRoomId(), messageDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
