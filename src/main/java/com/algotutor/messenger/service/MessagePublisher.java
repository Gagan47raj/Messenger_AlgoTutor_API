package com.algotutor.messenger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import com.algotutor.messenger.dto.MessageDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MessagePublisher {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private ChannelTopic roomMessageTopic;

	@Autowired
	private ChannelTopic privateMessageTopic;
	
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void publishRoomMessage(MessageDTO message) {
        try {
            String messageJson = objectMapper.writeValueAsString(message);
            redisTemplate.convertAndSend(roomMessageTopic.getTopic(), messageJson);
            System.out.println("Published room message to Redis: " + messageJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    
    public void publishPrivateMessage(MessageDTO message) {
        try {
            String messageJson = objectMapper.writeValueAsString(message);
            redisTemplate.convertAndSend(privateMessageTopic.getTopic(), messageJson);
            System.out.println("Published private message to Redis: " + messageJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
