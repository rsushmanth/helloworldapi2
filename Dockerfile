FROM --platform=linux/amd64 amazoncorretto:20.0.1
WORKDIR /app
EXPOSE 8080
COPY target/HelloWorldAPI-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=docker","-jar","app.jar"]