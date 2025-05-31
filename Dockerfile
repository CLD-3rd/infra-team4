FROM openjdk:17-jdk-slim
VOLUME /tmp
ARG JAR_FILE=target/roomMate.jar
COPY ${JAR_FILE} room.jar
ENTRYPOINT ["java", "-jar", "/room.jar"]
