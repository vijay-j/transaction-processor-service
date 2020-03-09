FROM gradle:6.2.2-jdk8 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon 

FROM openjdk:8-jdk-alpine
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/tps.jar 
ENTRYPOINT ["java","-jar","/app/tps.jar"]
