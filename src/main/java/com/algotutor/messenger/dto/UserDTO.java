package com.algotutor.messenger.dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.algotutor.messenger.entities.User;

import lombok.Data;

@Data
public class UserDTO {

	private String id;
	private String username;
	private String email;
	private Set<String> roles;
	private LocalDateTime createdAt;

	public UserDTO() {}

	public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = user.getRoles();
        this.createdAt = user.getCreatedAt();
    }
}
