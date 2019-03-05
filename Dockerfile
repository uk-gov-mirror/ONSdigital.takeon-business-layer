FROM maven:3.6.0-jdk-12-alpine

WORKDIR /cont_search_bl
COPY . /cont_search_bl
ENV eureka.instance.hostname=eurekaserver:8761
ENV eureka.instance.preferIpAddress=true
RUN mvn -Dmaven.test.skip=true clean package
CMD java -jar target/business-1.0-SNAPSHOT.jar
