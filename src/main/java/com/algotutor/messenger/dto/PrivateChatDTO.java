package com.algotutor.messenger.dto;

import java.time.LocalDateTime;

import com.algotutor.messenger.entities.PrivateChat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrivateChatDTO {

	private String id;
    private String chatId;
    private String participant1;
    private String participant2;
    private String participant1Username;
    private String participant2Username;
    private LocalDateTime createdAt;
    private int messageCount;
    private MessageDTO lastMessage;
    
    public PrivateChatDTO(PrivateChat chat) {
        this.id = chat.getId();
        this.chatId = chat.getChatId();
        this.participant1 = chat.getParticipant1();
        this.participant2 = chat.getParticipant2();
        this.participant1Username = chat.getParticipant1Username();
        this.participant2Username = chat.getParticipant2Username();
        this.createdAt = chat.getCreatedAt();
        this.messageCount = chat.getMessageIds() != null ? chat.getMessageIds().size() : 0;
    }
}
