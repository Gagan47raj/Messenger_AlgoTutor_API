# genz Messenger Backend

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen)
![Java](https://img.shields.io/badge/Java-17-orange)
![MongoDB](https://img.shields.io/badge/MongoDB-Latest-green)
![Redis](https://img.shields.io/badge/Redis-Latest-red)

Real-time chat application backend built with Spring Boot, featuring scalable messaging with Redis Pub/Sub and WebSocket integration.

## 🚀 Features

- **JWT Authentication**: Secure user registration and login
- **Real-time Messaging**: WebSocket with STOMP protocol
- **Room-based Chat**: Create and join public chat rooms
- **Private Messaging**: Direct messaging between users
- **Media Upload**: Support for image and file sharing
- **Redis Pub/Sub**: Scalable message broadcasting across multiple instances
- **MongoDB Integration**: Persistent message storage

## 🛠️ Technologies

- **Java 17**
- **Spring Boot 3.5.5**
- **Spring Security** with JWT
- **Spring WebSocket** with STOMP
- **MongoDB** for data persistence
- **Redis** for caching and message broker
- **Jackson** with JavaTimeModule for JSON serialization
- **Maven** for dependency management

## 📋 Prerequisites

- Java 17 or higher
- MongoDB running on `localhost:27017`
- Redis server running on `localhost:6379`
- Maven 3.6+

## ⚡ Quick Start

### 1. Clone the repository
