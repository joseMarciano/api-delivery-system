FROM gradle:7.5.1-jdk17
WORKDIR /home/app
COPY  . /home/app

CMD ["./gradlew","flywayMigrate","bootRun", "--args='--spring.profiles.active=development'"]

