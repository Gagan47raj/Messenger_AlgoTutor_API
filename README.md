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

git clone https://github.com/Gagan47raj/Messenger_AlgoTutor_API.git
cd Messenger_AlgoTutor_API


### 2. Configure application.properties
MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/genz_messenger

Redis Configuration
spring.redis.host=localhost
spring.redis.port=6379

JWT Configuration
jwt.secret=your-secret-key
jwt.expiration=86400000
### 3. Build and run
./mvnw clean package
java -jar target/genz-messenger-backend-0.0.1-SNAPSHOT.jar

The server will start on `http://localhost:8080`

## 📡 API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - User login
- `GET /api/users/me` - Get current user info

### Rooms
- `GET /api/rooms` - Get all rooms
- `POST /api/rooms` - Create new room
- `GET /api/rooms/{roomId}/messages` - Get room messages

### Private Chats
- `GET /api/private-chats` - Get user's private chats
- `POST /api/private-chats` - Create private chat
- `POST /api/private-chats/{chatId}/messages` - Send private message

### Media Upload
- `POST /api/upload` - Upload media file
- `POST /api/rooms/{roomId}/media` - Send media message

## 🔌 WebSocket Endpoints

- **Connection**: `/ws` - Main WebSocket endpoint
- **Room Messaging**: `/app/chat/{roomId}` - Send room message
- **Private Messaging**: `/app/private/{chatId}` - Send private message
- **Room Subscription**: `/topic/rooms/{roomId}` - Subscribe to room messages
- **Private Subscription**: `/topic/private/{chatId}` - Subscribe to private messages

## 🏗️ Architecture


┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐
│ Frontend │ │ Backend │ │ Database │
│ (React) │◄──►│ (Spring Boot) │◄──►│ (MongoDB) │
└─────────────────┘ └─────────────────┘ └─────────────────┘
▲ ▼
┌─────────────────┐
│ Redis │
│ (Pub/Sub) │
└─────────────────┘


## 🔧 Development

### Running Multiple Instances

Instance 1
java -jar target/app.jar --server.port=8080

Instance 2
java -jar target/app.jar --server.port=8081

Instance 3
java -jar target/app.jar --server.port=8082


### Testing WebSocket
Use a WebSocket client or browser dev tools to connect to `/ws` endpoint and test real-time messaging.

### Redis Pub/Sub Monitoring
redis-cli monitor


## 🐳 Docker Support

FROM openjdk:17-jdk-slim
COPY target/genz-messenger-backend-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]




## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🤝 Contributing

1. Fork the project
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request


