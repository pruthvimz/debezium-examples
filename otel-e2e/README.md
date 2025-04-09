Steps to run locally. 

0) Initiate non-business services in docker

    `cd debezium-examples/otel-e2e/deployment`

    `docker-compose -f docker-compose.yml up -d --scale service-one=0 --scale service-two=0`


1) Create database and schema

- Connect to postgres host, and create database and schema,

    `CREATE DATABASE sample_db;`

    `CREATE SCHEMA sample_schema;`

- `spring.jpa.hibernate.ddl-auto=create` is configured, so that at runtime, this will automatically create tables table_one and table_two as defined in the entity mappings.

2) Register debezium connector
- Register the debezium-event-business-connector using the cURL command provided in `essentials/debezium-event-business-connector.txt`

3) Run business services in docker

- Dockerize business services,

    `cd debezium-examples/otel-e2e`

    `docker build -f deployment/serviceOne.Dockerfile -t service-one:latest .`

    `docker build -f deployment/serviceTwo.Dockerfile -t service-two:latest .`

- Run business services,

    `cd debezium-examples/otel-e2e/deployment`

    `docker-compose -f docker-compose.yml up -d service-one service-two`
    
    Or just,
      
    `docker-compose -f docker-compose.yml up -d`

4) Testing

    `curl --location --request PUT 'http://localhost:8080/v1/service-one/record/sample1/sample2'`

- [service-one] This request inserts 'sample1' into the data field of table_one and 'sample2' into the data field of table_two. If the records already exist, they will be updated.

- [service-two] Debezium events will be consumed from the topic named 'debezium_event_business_topic'.
