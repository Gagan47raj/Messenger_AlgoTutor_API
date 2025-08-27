package com.algotutor.messenger.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.algotutor.messenger.dto.MessageDTO;
import com.algotutor.messenger.dto.SendMessageRequest;
import com.algotutor.messenger.entities.Message;
import com.algotutor.messenger.entities.User;
import com.algotutor.messenger.exception.UnauthorizedException;
import com.algotutor.messenger.repos.MessageRepository;
import com.algotutor.messenger.repos.UserRepository;

@Service
public class ChatService {

	@Autowired
	private MessageRepository messageRepo;
	
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate; 
	
	public MessageDTO sendMessage(String roomId, SendMessageRequest request) {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        // Verify room exists
        roomService.getRoomByRoomId(roomId);

        // Create and save message
        Message message = new Message(roomId, currentUser.getId(), 
                                    currentUser.getUsername(), request.getContent());
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
	
	 public MessageDTO sendMessageViaWebSocket(String roomId, SendMessageRequest request, String username) {
	        // Get user from database
	        User currentUser = userRepo.findByUsername(username)
	                .orElseThrow(() -> new UnauthorizedException("User not found: " + username));

	        // Verify room exists
	        roomService.getRoomByRoomId(roomId);

	        // Create and save message
	        Message message = new Message(roomId, currentUser.getId(), 
	                                    currentUser.getUsername(), request.getContent());
	        Message savedMessage = messageRepo.save(message);

	        // Add message to room
	        roomService.addMessageToRoom(roomId, savedMessage.getId());

	        // Create DTO for response and broadcast
	        MessageDTO messageDto = new MessageDTO(savedMessage);

	        // Broadcast message to WebSocket subscribers
	        messagingTemplate.convertAndSend("/topic/rooms/" + roomId, messageDto);

	        return messageDto;
	    }


}
