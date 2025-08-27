package com.algotutor.messenger.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.algotutor.messenger.dto.MessageDTO;
import com.algotutor.messenger.dto.SendMessageRequest;
import com.algotutor.messenger.service.ChatService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/rooms/{roomId}/messages")
@CrossOrigin(origins = "*")
public class ChatController {

	@Autowired
	private ChatService chatService;

	@PostMapping
    public ResponseEntity<MessageDTO> sendMessage(
            @PathVariable String roomId,
            @Valid @RequestBody SendMessageRequest request) {
        MessageDTO message = chatService.sendMessage(roomId, request);
        return ResponseEntity.ok(message);
    }

	@GetMapping
    public ResponseEntity<List<MessageDTO>> getRoomMessages(
            @PathVariable String roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        List<MessageDTO> messages = chatService.getRoomMessages(roomId, page, size);
        return ResponseEntity.ok(messages);
    }

}
