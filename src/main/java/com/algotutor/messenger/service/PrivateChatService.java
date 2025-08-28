package com.algotutor.messenger.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.algotutor.messenger.dto.CreatePrivateChatRequest;
import com.algotutor.messenger.dto.MessageDTO;
import com.algotutor.messenger.dto.PrivateChatDTO;
import com.algotutor.messenger.entities.PrivateChat;
import com.algotutor.messenger.entities.User;
import com.algotutor.messenger.exception.RoomNotFoundException;
import com.algotutor.messenger.exception.UserNotFoundException;
import com.algotutor.messenger.repos.MessageRepository;
import com.algotutor.messenger.repos.PrivateChatRepository;
import com.algotutor.messenger.repos.UserRepository;

@Service
public class PrivateChatService {

	@Autowired
	private PrivateChatRepository privateChatRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private MessageRepository messageRepo;

	public PrivateChatDTO createOrGetPrivateChat(CreatePrivateChatRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = (User) authentication.getPrincipal();

		// Find target user
		User targetUser = userRepo.findByUsername(request.getTargetUsername())
				.orElseThrow(() -> new UserNotFoundException("User not found: " + request.getTargetUsername()));

		// Check if chat already exists
		return privateChatRepo.findByParticipants(currentUser.getId(), targetUser.getId()).map(PrivateChatDTO::new)
				.orElseGet(() -> {
					// Create new private chat
					PrivateChat newChat = new PrivateChat(currentUser.getId(), targetUser.getId(),
							currentUser.getUsername(), targetUser.getUsername());
					PrivateChat savedChat = privateChatRepo.save(newChat);
					return new PrivateChatDTO(savedChat);
				});
	}
	
	public List<PrivateChatDTO> getUserPrivateChats() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        
        List<PrivateChat> chats = privateChatRepo.findByParticipant(currentUser.getId());
        
        return chats.stream()
                .map(chat -> {
                    PrivateChatDTO dto = new PrivateChatDTO(chat);
                    // Get last message if exists
                    if (!chat.getMessageIds().isEmpty()) {
                        String lastMessageId = chat.getMessageIds().get(chat.getMessageIds().size() - 1);
                        messageRepo.findById(lastMessageId).ifPresent(message -> 
                            dto.setLastMessage(new MessageDTO(message))
                        );
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }
	
	public PrivateChat getPrivateChatById(String chatId) {
        return privateChatRepo.findByChatId(chatId)
                .orElseThrow(() -> new RoomNotFoundException("Private chat not found: " + chatId));
    }
    
    public boolean isUserInChat(String chatId, String userId) {
        return privateChatRepo.findByChatId(chatId)
                .map(chat -> chat.isParticipant(userId))
                .orElse(false);
    }

    public void addMessageToChat(String chatId, String messageId) {
        PrivateChat chat = getPrivateChatById(chatId);
        chat.getMessageIds().add(messageId);
        privateChatRepo.save(chat);
    }

}
