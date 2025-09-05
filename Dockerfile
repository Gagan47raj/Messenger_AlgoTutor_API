FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/AlgoTutor_Messenger-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
