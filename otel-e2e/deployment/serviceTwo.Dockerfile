FROM openjdk:11-jre-slim
COPY codebase/otel-e2e/service-two/target/service-two.jar service-two.jar
CMD java -jar service-two.jar
