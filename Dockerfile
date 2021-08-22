FROM maven:3.8.2-openjdk-16
COPY . /app
WORKDIR /app
RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:16
COPY --from=0 /app/pava/target/pava-1.0-SNAPSHOT-jar-with-dependencies.jar /usr/local/pdk/bin/pava.jar
COPY --from=1 /app/pavac/target/pavac-1.0-SNAPSHOT-jar-with-dependencies.jar /usr/local/pdk/bin/pavac.jar
WORKDIR /usr/local/pdk/bin