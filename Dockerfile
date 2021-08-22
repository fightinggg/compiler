FROM maven:3.8.2-openjdk-16
COPY . /app
WORKDIR /app
RUN mvn clean package -Dmaven.test.skip=true