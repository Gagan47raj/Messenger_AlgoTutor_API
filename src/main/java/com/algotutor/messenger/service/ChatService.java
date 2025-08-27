package com.algotutor.messenger.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.algotutor.messenger.dto.MessageDTO;
import com.algotutor.messenger.dto.SendMessageRequest;
import com.algotutor.messenger.entities.Message;
import com.algotutor.messenger.repos.MessageRepository;

@Service
public class ChatService {

	@Autowired
	private MessageRepository messageRepo;
	
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate; 
	
	public MessageDTO sendMessage(String roomId, SendMessageRequest request) {
        // Verify room exists
        roomService.getRoomByRoomId(roomId);

        // Create and save message
        Message message = new Message(roomId, request.getSender(), request.getContent());
        Message savedMessage = messageRepo.save(message);

        // Add message to room
        roomService.addMessageToRoom(roomId, savedMessage.getId());

        // Create DTO for response and broadcast
        MessageDTO messageDto = new MessageDTO(savedMessage);

        // Broadcast message to WebSocket subscribers
        messagingTemplate.convertAndSend("/topic/rooms/" + roomId, messageDto);

        return messageDto;
    }
	
	public List<MessageDTO> getRoomMessages(String roomId, int page, int size) {
        // Verify room exists
        roomService.getRoomByRoomId(roomId);

        Pageable pageable = PageRequest.of(page, size);
        Page<Message> messages = messageRepo.findByRoomIdOrderByTimestampDesc(roomId, pageable);

        return messages.getContent()
                .stream()
                .map(MessageDTO::new)
                .collect(Collectors.toList());
    }


}
