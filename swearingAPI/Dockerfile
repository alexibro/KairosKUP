FROM maven as builder
COPY . /code
WORKDIR /code
RUN mvn -DskipTests=true clean package

FROM openjdk:8-jre
COPY --from=builder /code/target/swearingAPI-0.0.1-SNAPSHOT.jar /app/swearingAPI-0.0.1-SNAPSHOT.jar
WORKDIR /app
EXPOSE 8000
CMD ["java", "-jar", "swearingAPI-0.0.1-SNAPSHOT.jar"]