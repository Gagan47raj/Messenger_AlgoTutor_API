package com.algotutor.messenger.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.algotutor.messenger.dto.MessageDTO;
import com.algotutor.messenger.dto.SendMediaMessageRequest;
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
	
	@Autowired
	private PrivateChatService privateChatService; 
	
	// Add these fields
	@Value("${file.upload.dir:/uploads}")
	private String uploadDir;

	@Value("${server.port:8080}")
	private String serverPort;
	
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
	 
	 public MessageDTO sendMediaMessage(String roomId, SendMediaMessageRequest request) {
		    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		    User currentUser = (User) authentication.getPrincipal();

		    // Verify room exists
		    roomService.getRoomByRoomId(roomId);

		    // Create media message
		    Message message = new Message(
		        roomId, currentUser.getId(), currentUser.getUsername(), 
		        request.getContent(), request.getMessageType(), request.getMediaUrl(),
		        request.getMediaFileName(), request.getMediaFileSize(), request.getMimeType()
		    );
		    
		    Message savedMessage = messageRepo.save(message);
		    roomService.addMessageToRoom(roomId, savedMessage.getId());

		    MessageDTO messageDto = new MessageDTO(savedMessage);
		    messagingTemplate.convertAndSend("/topic/rooms/" + roomId, messageDto);

		    return messageDto;
		}
	 
	 public MessageDTO sendPrivateMessage(String chatId, SendMessageRequest request) {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        User currentUser = (User) authentication.getPrincipal();

	        if (!privateChatService.isUserInChat(chatId, currentUser.getId())) {
	            throw new UnauthorizedException("User not authorized for this chat");
	        }

	        Message message = new Message(chatId, currentUser.getId(), 
	                                    currentUser.getUsername(), request.getContent());
	        Message savedMessage = messageRepo.save(message);

	        privateChatService.addMessageToChat(chatId, savedMessage.getId());

	        MessageDTO messageDto = new MessageDTO(savedMessage);
	        messagingTemplate.convertAndSend("/topic/private/" + chatId, messageDto);

	        return messageDto;
	    }
	 
	 public MessageDTO sendPrivateMessageViaWebSocket(String chatId, SendMessageRequest request, String username) {
	        // Get user from database using the provided username
	        User currentUser = userRepo.findByUsername(username)
	                .orElseThrow(() -> new UnauthorizedException("User not found: " + username));

	        // Verify user is participant in this chat
	        if (!privateChatService.isUserInChat(chatId, currentUser.getId())) {
	            throw new UnauthorizedException("User not authorized for this chat");
	        }

	        // Create and save message
	        Message message = new Message(chatId, currentUser.getId(), 
	                                    currentUser.getUsername(), request.getContent());
	        Message savedMessage = messageRepo.save(message);

	        // Add message to chat
	        privateChatService.addMessageToChat(chatId, savedMessage.getId());

	        MessageDTO messageDto = new MessageDTO(savedMessage);
	        
	        // Broadcast to private chat topic
	        messagingTemplate.convertAndSend("/topic/private/" + chatId, messageDto);

	        return messageDto;
	    }

	 
	 public List<MessageDTO> getPrivateChatMessages(String chatId, int page, int size) {
		    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		    User currentUser = (User) authentication.getPrincipal();

		    // Verify user is participant in this chat
		    if (!privateChatService.isUserInChat(chatId, currentUser.getId())) {
		        throw new UnauthorizedException("User not authorized for this chat");
		    }

		    Pageable pageable = PageRequest.of(page, size);
		    Page<Message> messages = messageRepo.findByRoomIdOrderByTimestampDesc(chatId, pageable);

		    return messages.getContent()
		            .stream()
		            .map(MessageDTO::new)
		            .collect(Collectors.toList());
		}

	 
	 public String uploadFile(MultipartFile file) throws IOException {
		    // Create upload directory if it doesn't exist
		    Path uploadPath = Paths.get(uploadDir);
		    if (!Files.exists(uploadPath)) {
		        Files.createDirectories(uploadPath);
		    }

		    // Generate unique filename
		    String originalFileName = file.getOriginalFilename();
		    String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
		    String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

		    // Save file
		    Path filePath = uploadPath.resolve(uniqueFileName);
		    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

		    // Return URL
		    return "http://localhost:" + serverPort + "/uploads/" + uniqueFileName;
		}


}
