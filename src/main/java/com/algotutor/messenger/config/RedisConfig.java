package com.algotutor.messenger.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.algotutor.messenger.service.PrivateMessageSubscriber;
import com.algotutor.messenger.service.RoomMessageSubscriber;

@Configuration
public class RedisConfig {

	@Value("${spring.redis.host:localhost}")
	private String redisHost;

	@Value("${spring.redis.port:6379}")
	private int redisPort;

	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
		config.setHostName(redisHost);
		config.setPort(redisPort);
		return new JedisConnectionFactory(config);
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(jedisConnectionFactory());

		// Use String serializer for keys
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());

		// Use JSON serializer for values
		Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
		template.setValueSerializer(serializer);
		template.setHashValueSerializer(serializer);

		template.afterPropertiesSet();
		return template;
	}

	@Bean
	public ChannelTopic roomMessageTopic() {
		return new ChannelTopic("room-messages");
	}

	@Bean
	public ChannelTopic privateMessageTopic() {
		return new ChannelTopic("private-messages");
	}
	
	@Bean
    public RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(messageListener(), roomMessageTopic());
        container.addMessageListener(privateMessageListener(), privateMessageTopic());
        return container;
    }
	
	@Bean
    public MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(new RoomMessageSubscriber(), "receiveMessage");
    }

    @Bean
    public MessageListenerAdapter privateMessageListener() {
        return new MessageListenerAdapter(new PrivateMessageSubscriber(), "receiveMessage");
    }
}
