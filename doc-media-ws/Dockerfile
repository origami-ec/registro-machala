FROM adoptopenjdk/openjdk11:alpine-jre

COPY ./target/origami-media-ws-1.0.0.jar /opt/app/ws-media.jar

WORKDIR /opt/app
RUN sh -c 'touch ws-media.jar'

EXPOSE 8784

ENV JAVA_OPTS="-server --add-exports java.desktop/sun.font=ALL-UNNAMED"

# Arranque dinámico con variables externas
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -Dspring.cloud.config.uri=${SPRING_CLOUD_CONFIG_URI} -jar ws-media.jar"]
