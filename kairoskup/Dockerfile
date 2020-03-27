FROM maven as builder
COPY . /code
WORKDIR /code
RUN mvn clean package

FROM openjdk:8-jre
COPY --from=builder /code/target/kairoskup-0.0.1-SNAPSHOT.jar /app/kairoskup-0.0.1-SNAPSHOT.jar
WORKDIR /app
EXPOSE 8080
CMD ["java", "-jar", "kairoskup-0.0.1-SNAPSHOT.jar"]
