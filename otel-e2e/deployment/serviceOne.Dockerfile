FROM openjdk:11-jre-slim
COPY codebase/otel-e2e/service-one/target/service-one.jar service-one.jar
CMD java -jar service-one.jar
