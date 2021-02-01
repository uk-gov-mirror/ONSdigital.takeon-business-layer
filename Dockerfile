#FROM maven:3.6.0-jdk-12-alpine

#WORKDIR /cont_search_bl
#COPY . /cont_search_bl
#ENV eureka.instance.hostname=eurekaserver:8761
#ENV eureka.instance.preferIpAddress=true
#RUN mvn -Dmaven.test.skip=true clean package
#CMD java -jar target/business-1.0-SNAPSHOT.jar



# our base build image
FROM maven:3.6.2-jdk-11-slim as maven

# copy the project files
COPY ./pom.xml ./pom.xml

# build all dependencies
RUN mvn dependency:go-offline -B

# Build our application
COPY ./src ./src
RUN mvn -Dmaven.test.skip=true -Dcheckstyle.skip clean package


# our final base image
FROM openjdk:11-jre-slim

WORKDIR /takeonbl
COPY --from=maven target/business-1.0-SNAPSHOT.jar  ./takeonbl/

# set the startup command to run your binary
# CMD ["java", "-jar", "./takeonbl/business-1.0-SNAPSHOT.jar"]
# set the startup command to run your binary
CMD ["java", "-Xmx1024m", "-Xms1024m", "-jar", "./takeonbl/business-1.0-SNAPSHOT.jar"]
