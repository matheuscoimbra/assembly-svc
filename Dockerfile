FROM adoptopenjdk/openjdk11:jdk-11.0.1.13-slim
ENV TZ America/Fortaleza
ENV LANG=C.UTF-8
COPY build/libs/*.jar app.jar
ENTRYPOINT ["sh","-c","java -jar /app.jar"]
EXPOSE 8080