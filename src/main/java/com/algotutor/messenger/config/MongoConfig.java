package com.algotutor.messenger.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.algotutor.messenger.repos")
@EnableMongoAuditing
public class MongoConfig{
	
}
