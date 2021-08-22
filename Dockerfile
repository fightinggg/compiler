FROM 3.8.2-openjdk-16
RUN mvn clean package -Dmaven.test.skip=true