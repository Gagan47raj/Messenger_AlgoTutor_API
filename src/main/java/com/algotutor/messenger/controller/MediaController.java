package com.algotutor.messenger.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.algotutor.messenger.dto.MessageDTO;
import com.algotutor.messenger.dto.SendMediaMessageRequest;
import com.algotutor.messenger.entities.Message.MessageType;
import com.algotutor.messenger.service.ChatService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MediaController {

	@Autowired
    private ChatService chatService;
	
	@PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = chatService.uploadFile(file);
            
            Map<String, Object> response = new HashMap<>();
            response.put("url", fileUrl);
            response.put("filename", file.getOriginalFilename());
            response.put("size", file.getSize());
            response.put("mimeType", file.getContentType());
            
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to upload file: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
	
	@PostMapping("/rooms/{roomId}/media")
    public ResponseEntity<MessageDTO> sendMediaMessage(
            @PathVariable String roomId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String content,
            @RequestParam MessageType messageType) {
        
        try {
            String mediaUrl = chatService.uploadFile(file);
            
            SendMediaMessageRequest request = new SendMediaMessageRequest();
            request.setContent(content);
            request.setMessageType(messageType);
            request.setMediaUrl(mediaUrl);
            request.setMediaFileName(file.getOriginalFilename());
            request.setMediaFileSize(file.getSize());
            request.setMimeType(file.getContentType());
            
            MessageDTO message = chatService.sendMediaMessage(roomId, request);
            return ResponseEntity.ok(message);
            
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
