FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} estudiante-service.jar
ENTRYPOINT ["java","-jar","/estudiante-service.jar"]