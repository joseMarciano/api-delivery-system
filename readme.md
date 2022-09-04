## Microservice: Managing orders and drivers ( Back-end ).

## :pushpin: Application purpose
- This application is a part of DELIVERY SYSTEM, a project that simulates a delivery system flux
- To improve my skills on Spring Boot
- To improve my knowledge's in microservices
- To improve my knowledge about RabbitMq
- Apply concepts about TDD and DDD
- Apply clean architecture


## :wrench: Architecture fluxogram
<div style="background: white"> 
    <img src="/public/diagram-delivery-system.png">
</div>

## :ballot_box_with_check: Progress of the project

- [x] Initialize application
- [x] Create drivers
- [x] Create orders
- [x] Create rabbitmq integration

## :scroll: How you can run this application?

- You need to run ``docker-compose up -d`` on the root application
- You need to run ``./gradlew flywayMigrate`` to run migrations inside the container