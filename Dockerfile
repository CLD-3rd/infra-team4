FROM openjdk:17-jdk-slim
VOLUME /tmp
ARG JAR_FILE=target/room-reservation-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} room.jar
ENTRYPOINT ["java", "-jar", "/room.jar"]
