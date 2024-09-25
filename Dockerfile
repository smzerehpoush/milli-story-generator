FROM eclipse-temurin:17
ENV LANG=C.UTF-8 \
    LC_ALL=C.UTF-8 \
    TZ=Asia/Tehran

WORKDIR /opt

COPY target/milli-instagram-story-generator-1.0-SNAPSHOT.jar app.jar

CMD [ "java", "-jar", "app.jar" ]
