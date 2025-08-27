package com.algotutor.messenger.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.algotutor.messenger.dto.CreateRoomRequest;
import com.algotutor.messenger.dto.RoomDTO;
import com.algotutor.messenger.entities.Room;
import com.algotutor.messenger.entities.User;
import com.algotutor.messenger.exception.RoomNotFoundException;
import com.algotutor.messenger.repos.RoomRepository;
import com.mongodb.DuplicateKeyException;

@Service
public class RoomService {

	@Autowired
	private RoomRepository roomRepo;
	
	public RoomDTO createRoom(CreateRoomRequest request)
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
		 try {
		        Room room = new Room(request.getName(), currentUser.getId(), request.getRoomId());
		        Room savedRoom = roomRepo.save(room);
		        return new RoomDTO(savedRoom);
		    } catch (DuplicateKeyException ex) {
		        throw new IllegalArgumentException("Room ID already exists: " + request.getRoomId());
		    }
	}
	
	
	public List<RoomDTO> getAllRooms() {
        return roomRepo.findAll()
                .stream()
                .map(RoomDTO::new)
                .collect(Collectors.toList());
    }
	
	public Room getRoomByRoomId(String roomId) {
        return roomRepo.findByRoomId(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found with roomId: " + roomId));
    }
	
	public void addMessageToRoom(String roomId, String messageId) {
        Room room = getRoomByRoomId(roomId);
        room.getMessageIds().add(messageId);
        roomRepo.save(room);
    }
}
