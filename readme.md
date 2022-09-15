## Microservice: Managing orders and drivers ( Back-end ).

- This application is a part of DELIVERY SYSTEM, a project that simulates a delivery system flux

## :pushpin: Application purpose

- To improve my skills on Spring Boot
- To improve my knowledge's in microservices
- To improve my knowledge about RabbitMq
- Apply concepts about TDD and DDD
- Apply clean architecture

This application is responsible to manages the orders and drivers

## :wrench: Architecture fluxogram
<div style="background: white"> 
    <img src="/public/diagram.svg">
</div>

## :ballot_box_with_check: Progress of the project

- [x] Initialize application
- [x] Create drivers
- [x] Create orders
- [x] Create rabbitmq integration

## :computer: Technologies

![Badge](https://img.shields.io/badge/RabbitMQ--%233178C6?style=for-the-badge&logo=RabbitMQ&color=ff6600)
![Badge](https://img.shields.io/badge/Spring_Boot--%2361DAFB?style=for-the-badge&logo=Springboot&color=6DB33F)
![Badge](https://img.shields.io/badge/Docker--%2361DAFB?style=for-the-badge&logo=Docker&color=2496ED)
![Badge](https://img.shields.io/badge/PostgreSQL--%2361DAFB?style=for-the-badge&logo=PostgreSQL&color=4169E1)
![Badge](https://img.shields.io/badge/Gradle--%2361DAFB?style=for-the-badge&logo=Gradle&color=02303A)

## :rocket: How to run the application

- In the root directory run ` ./gradlew bootRun --args='--spring.profiles.active=development'`

## Other project services

- [web-socket-delivery-system](https://github.com/joseMarciano/api-delivery-system-web-socket)
- [api-delivery-system-cordinator](https://github.com/joseMarciano/api-delivery-system-cordinator)
- [front-delivery-system](https://github.com/joseMarciano/front-delivery-system)
