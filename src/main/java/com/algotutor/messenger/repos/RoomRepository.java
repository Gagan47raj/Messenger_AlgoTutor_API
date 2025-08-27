package com.algotutor.messenger.repos;

import org.springframework.stereotype.Repository;

import com.algotutor.messenger.entities.Room;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;


@Repository
public interface RoomRepository extends MongoRepository<Room, String>{

	 Optional<Room> findByRoomId(String roomId);
}
