FROM openjdk:8
ADD target/customer-0.0.1-SNAPSHOT.jar customer-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","customer-0.0.1-SNAPSHOT.jar"]