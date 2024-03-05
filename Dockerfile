FROM gradle:8.4-jdk17 AS builder

COPY ./ /home/gradle/project

WORKDIR /home/gradle/project

RUN gradle -Dorg.gradle.daemon=false clean build --stacktrace --info

RUN mkdir -p /home/gradle/project/build/distributions/app/

RUN unzip /home/gradle/project/build/distributions/*.zip -d /home/gradle/project/build/distributions/app/

FROM openjdk:17-jdk-slim

COPY --from=builder /home/gradle/project/build/distributions/app/ /opt/app/

WORKDIR /opt/app

RUN rm -rf /var/cache/*

EXPOSE 8080

CMD ["/opt/app/rinha-backend-2024-q1-kotlin-1.0/bin/rinha-backend-2024-q1-kotlin"]
