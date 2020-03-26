FROM openjdk:8-jre

COPY /kairoskup/target/kairoskup-0.0.1-SNAPSHOT.jar /

EXPOSE 8080

CMD ["java", "-jar", "kairoskup-0.0.1-SNAPSHOT.jar"]