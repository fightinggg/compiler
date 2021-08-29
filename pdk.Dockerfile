FROM centos:8
WORKDIR /app
RUN yum install wget -y
RUN wget https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-21.2.0/graalvm-ce-java16-linux-amd64-21.2.0.tar.gz

FROM maven:3.8.2-openjdk-16
WORKDIR /app
COPY dockerCache.xml /app/pom.xml
RUN mvn dependency:go-offline
COPY . /app
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
COPY main/src/main/resources/config /target/config
COPY pavac/src/main/resources/code.pava /target/example/code.pava
COPY pava/src/main/resources/code.par /target/example/code.par
RUN mkdir /target/bin
RUN mv pava /target/bin/pava
RUN mv pavac /target/bin/pavac

FROM alpine:3.14
ENV GLIBC_VERSION=2.27-r0 \
    PAVA_HOME=/usr/local/pdk \
    PATH=/usr/local/pdk/bin:$PATH
RUN wget -q -O /etc/apk/keys/sgerrand.rsa.pub https://alpine-pkgs.sgerrand.com/sgerrand.rsa.pub \
    &&  wget "https://github.com/sgerrand/alpine-pkg-glibc/releases/download/$GLIBC_VERSION/glibc-$GLIBC_VERSION.apk" \
    &&  apk --no-cache add "glibc-$GLIBC_VERSION.apk" \
    &&  rm "glibc-$GLIBC_VERSION.apk" \
    &&  wget "https://github.com/sgerrand/alpine-pkg-glibc/releases/download/$GLIBC_VERSION/glibc-bin-$GLIBC_VERSION.apk" \
    &&  apk --no-cache add "glibc-bin-$GLIBC_VERSION.apk" \
    &&  rm "glibc-bin-$GLIBC_VERSION.apk" \
    &&  wget "https://github.com/sgerrand/alpine-pkg-glibc/releases/download/$GLIBC_VERSION/glibc-i18n-$GLIBC_VERSION.apk" \
    &&  apk --no-cache add "glibc-i18n-$GLIBC_VERSION.apk" \
    &&  rm "glibc-i18n-$GLIBC_VERSION.apk"
COPY --from=2 /target $PAVA_HOME
WORKDIR $PAVA_HOME/example
