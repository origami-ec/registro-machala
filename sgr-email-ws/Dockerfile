FROM adoptopenjdk/openjdk11:alpine-jre
COPY ./target/sigeri-email-0.0.1-SNAPSHOT.jar /opt/app/sigeri-email.jar
WORKDIR /opt/app
RUN sh -c 'touch sigeri-email.jar'

EXPOSE 8782
ENV JAVA_OPTS="-server --add-exports java.desktop/sun.font=ALL-UNNAMED"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -Dspring.cloud.config.uri=${SPRING_CLOUD_CONFIG_URI} -jar sigeri-email.jar"]
