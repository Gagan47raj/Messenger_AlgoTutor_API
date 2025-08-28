package com.algotutor.messenger.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.algotutor.messenger.dto.CreatePrivateChatRequest;
import com.algotutor.messenger.dto.MessageDTO;
import com.algotutor.messenger.dto.PrivateChatDTO;
import com.algotutor.messenger.dto.SendMessageRequest;
import com.algotutor.messenger.service.ChatService;
import com.algotutor.messenger.service.PrivateChatService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/private-chats")
@CrossOrigin(origins = "*")
public class PrivateChatController {

	@Autowired
	private PrivateChatService privateChatService;

	@Autowired
	private ChatService chatService;

	@PostMapping
	public ResponseEntity<PrivateChatDTO> createOrGetPrivateChat(@Valid @RequestBody CreatePrivateChatRequest request) {
		PrivateChatDTO chat = privateChatService.createOrGetPrivateChat(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(chat);
	}
	
	@GetMapping
    public ResponseEntity<List<PrivateChatDTO>> getUserPrivateChats() {
        List<PrivateChatDTO> chats = privateChatService.getUserPrivateChats();
        return ResponseEntity.ok(chats);
    }
	
	@PostMapping("/{chatId}/messages")
    public ResponseEntity<MessageDTO> sendPrivateMessage(
            @PathVariable String chatId,
            @Valid @RequestBody SendMessageRequest request) {
        MessageDTO message = chatService.sendPrivateMessage(chatId, request);
        return ResponseEntity.ok(message);
    }
	
	@GetMapping("/{chatId}/messages")
    public ResponseEntity<List<MessageDTO>> getPrivateChatMessages(
            @PathVariable String chatId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        List<MessageDTO> messages = chatService.getPrivateChatMessages(chatId, page, size);
        return ResponseEntity.ok(messages);
    }
	
	
	

}
