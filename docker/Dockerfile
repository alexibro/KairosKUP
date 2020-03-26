FROM maven as builder
COPY ../kairoskup /code
WORKDIR /code
RUN mvn -DskipTests=true package

FROM openjdk:8-jre
COPY --from=builder /code/target/*.jar /app/
COPY ./run.sh /app/
RUN chmod 777 /app/run.sh
EXPOSE 8080
CMD ["/app/run.sh"]
