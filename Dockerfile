FROM eclipse-temurin:17
ENV LANG=C.UTF-8 \
    LC_ALL=C.UTF-8 \
    TZ=Asia/Tehran

RUN apt-get update -qq && \
    apt-get install -qq --no-install-recommends \
        curl \
        jq \
        unzip \
        vim \
    # Cleanup
    && \
    apt-get autoremove -y && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /opt

COPY milli-instagram-story-generator-1.0-SNAPSHOT.jar app.jar

CMD [ "java", "-jar", "app.jar" ]
