FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} examen-service.jar
ENTRYPOINT ["java","-jar","/examen-service.jar"]