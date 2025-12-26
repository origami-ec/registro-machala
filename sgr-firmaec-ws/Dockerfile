
FROM adoptopenjdk/openjdk11:alpine-jre
COPY ./target/sigeri-firmaec-0.0.1-SNAPSHOT.jar /opt/app/sigeri-firmaec.jar
WORKDIR /opt/app
RUN sh -c 'touch sigeri-firmaec.jar'
EXPOSE 8781
ENV JAVA_OPTS="-server --add-exports java.desktop/sun.font=ALL-UNNAMED"
ENTRYPOINT ["java","-jar","sigeri-firmaec.jar"]