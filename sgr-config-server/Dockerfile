FROM adoptopenjdk/openjdk11:alpine-jre
COPY ./target/sgr-config-server-0.0.1-SNAPSHOT.jar /opt/app/sgr-config-server.jar
WORKDIR /opt/app
RUN sh -c 'touch sgr-config-server.jar'
EXPOSE 8888
ENTRYPOINT ["java","-Dspring.profiles.active=dev","-jar","sgr-config-server.jar"]
