package com.algotutor.messenger.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "rooms")
@Data
@NoArgsConstructor
public class Room {

    @Id
    private String id;

    @Indexed(unique = true)
    private String roomId;

    private String name;
    private String createdBy;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private List<String> messageIds = new ArrayList<>();
    private Set<String> participants = new HashSet<>();

    public Room(String name, String roomId) {
        this();
        this.name = name;
        this.roomId = roomId;
        this.createdAt = LocalDateTime.now();
    }

    public Room(String name, String createdBy, String roomId) {
        this();
        this.name = name;
        this.createdBy = createdBy;
        this.roomId = roomId;
        this.createdAt = LocalDateTime.now();
        this.participants.add(createdBy); // Creator auto-added
    }
}
