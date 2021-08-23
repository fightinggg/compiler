FROM centos:8
WORKDIR /app
RUN yum install wget -y
RUN wget https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-21.2.0/graalvm-ce-java16-linux-amd64-21.2.0.tar.gz

FROM maven:3.8.2-openjdk-16
COPY . /app
WORKDIR /app
RUN mvn clean package -Dmaven.test.skip=true

FROM centos:8
WORKDIR /app
COPY --from=0 /app/graalvm-ce-java16-linux-amd64-21.2.0.tar.gz /app/graalvm-ce-java16-linux-amd64-21.2.0.tar.gz
RUN tar -zxvf graalvm-ce-java16-linux-amd64-21.2.0.tar.gz
ENV PATH $PATH:/app/graalvm-ce-java16-21.2.0/bin
RUN gu install native-image
RUN yum install gcc glibc-devel zlib-devel -y
COPY --from=1 /app/pava/target/pava-1.0-SNAPSHOT-jar-with-dependencies.jar /app/pava.jar
COPY --from=1 /app/pavac/target/pavac-1.0-SNAPSHOT-jar-with-dependencies.jar /app/pavac.jar
RUN native-image -cp pava.jar -H:Class=com.example.client.pava.Pava -H:Name=pava -H:+ReportUnsupportedElementsAtRuntime
RUN native-image -cp pavac.jar -H:Class=com.example.client.pava.Pavac -H:Name=pavac -H:+ReportUnsupportedElementsAtRuntime

FROM alpine:3
COPY --from=2 /app/pava /usr/bin/pava
COPY --from=2 /app/pavac /usr/bin/pavac

