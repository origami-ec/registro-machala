FROM adoptopenjdk/openjdk11:alpine-jre
COPY ./target/origami-zull-ws-0.0.1-SNAPSHOT.jar /opt/app/ws-zull.jar

WORKDIR /opt/app
RUN sh -c 'touch ws-zull.jar'

EXPOSE 8783

ENV JAVA_OPTS="-server --add-exports java.desktop/sun.font=ALL-UNNAMED"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -Dspring.cloud.config.uri=${SPRING_CLOUD_CONFIG_URI} -jar ws-zull.jar"]
