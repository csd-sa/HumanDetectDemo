FROM openjdk:8

COPY ./target/*.jar /usr/local/src/
WORKDIR /usr/local/src/

CMD ["java", "-jar", "neuhub-demo-1.0.0.jar"] 
