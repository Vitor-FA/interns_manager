FROM openjdk:17
RUN mkdir app
ARG JAR_FILE
ADD /target/${JAR_FILE} /app/interns.manager.jar
WORKDIR /app
ENTRYPOINT java -jar interns.manager.jar