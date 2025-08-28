package com.algotutor.messenger.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.algotutor.messenger.entities.PrivateChat;

@Repository
public interface PrivateChatRepository extends MongoRepository<PrivateChat, String> {

	Optional<PrivateChat> findByChatId(String chatId);

	@Query("{'$or': [{'participant1': ?0}, {'participant2': ?0}]}")
	List<PrivateChat> findByParticipant(String userId);

	@Query("{'$or': [" + "{'participant1': ?0, 'participant2': ?1}, " + "{'participant1': ?1, 'participant2': ?0}"
			+ "]}")
	Optional<PrivateChat> findByParticipants(String userId1, String userId2);

}
