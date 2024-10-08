FROM openjdk:17-oracle

ENV TZ=Asia/Tehran

WORKDIR /opt

COPY target/milli-instagram-story-generator-1.0-SNAPSHOT.jar app.jar

CMD [ "java", "-jar", "app.jar" ]
