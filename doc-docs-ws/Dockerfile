FROM adoptopenjdk/openjdk11:alpine-jre

COPY ./target/origami-docs-ws-1.0.0.jar /opt/app/ws-docs.jar

WORKDIR /opt/app
RUN sh -c 'touch ws-docs.jar'

EXPOSE 8718

ENV JAVA_OPTS="-server --add-exports java.desktop/sun.font=ALL-UNNAMED"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -Dspring.cloud.config.uri=${SPRING_CLOUD_CONFIG_URI} -jar ws-docs.jar"]
