package com.algotutor.messenger.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "private_chats")
@Data
@NoArgsConstructor
@CompoundIndex(def = "{'participant1': 1, 'participant2': 1}", unique = true)
public class PrivateChat {

	@Id
	private String id;

	@Indexed(unique = true)
	private String chatId;

	private String participant1; // User ID (smaller one)
	private String participant2; // User ID (larger one)
	private String participant1Username;
	private String participant2Username;

	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	private List<String> messageIds = new ArrayList<>();

	public PrivateChat(String participant1, String participant2, String participant1Username,
			String participant2Username) {
// Ensure consistent ordering for uniqueness
		if (participant1.compareTo(participant2) < 0) {
			this.participant1 = participant1;
			this.participant2 = participant2;
			this.participant1Username = participant1Username;
			this.participant2Username = participant2Username;
		} else {
			this.participant1 = participant2;
			this.participant2 = participant1;
			this.participant1Username = participant2Username;
			this.participant2Username = participant1Username;
		}

		this.chatId = generateChatId(this.participant1, this.participant2);
		this.createdAt = LocalDateTime.now();
	}
	
	private String generateChatId(String userId1, String userId2) {
        return "private_" + userId1 + "_" + userId2;
    }
    
    public boolean isParticipant(String userId) {
        return participant1.equals(userId) || participant2.equals(userId);
    }
    
    public String getOtherParticipant(String userId) {
        return participant1.equals(userId) ? participant2 : participant1;
    }
    
    public String getOtherParticipantUsername(String userId) {
        return participant1.equals(userId) ? participant2Username : participant1Username;
    }

}
