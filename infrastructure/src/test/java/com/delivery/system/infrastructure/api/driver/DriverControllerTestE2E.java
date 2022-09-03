package com.delivery.system.infrastructure.api.driver;

import com.delivery.system.E2ETest;
import com.delivery.system.configs.json.Json;
import com.delivery.system.domain.driver.Driver;
import com.delivery.system.domain.driver.DriverID;
import com.delivery.system.infrastructure.driver.models.create.CreateDriverRequest;
import com.delivery.system.infrastructure.driver.models.create.CreateDriverResponse;
import com.delivery.system.infrastructure.driver.models.findById.FindDriverByIdResponse;
import com.delivery.system.infrastructure.driver.models.update.UpdateDriverRequest;
import com.delivery.system.infrastructure.driver.persistence.DriverJpaEntity;
import com.delivery.system.infrastructure.driver.persistence.DriverRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.function.Function;

@E2ETest
@Testcontainers
public class DriverControllerTestE2E {

    private static final String BASE_PATH = "/drivers";

    @Container
    public static PostgreSQLContainer<?> postgresDB =
            new PostgreSQLContainer<>("postgres:14.5")
                    .withDatabaseName("order_manager")
                    .withUsername("order_owner")
                    .withPassword("a1b2c3f4e5");

    @Container
    public static RabbitMQContainer rabbitContainer =
            new RabbitMQContainer("rabbitmq:3.10.7");


    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresDB::getJdbcUrl);
        registry.add("spring.datasource.username", postgresDB::getUsername);
        registry.add("spring.datasource.password", postgresDB::getPassword);
        registry.add("spring.datasource.port", () -> postgresDB.getMappedPort(5432));

        registry.add("spring.rabbitmq.host", rabbitContainer::getHost);
        registry.add("spring.rabbitmq.username", rabbitContainer::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbitContainer::getAdminPassword);
        registry.add("spring.rabbitmq.port", () -> rabbitContainer.getMappedPort(5672));
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private DriverRepository driverRepository;

    @Test
    public void asAUser_IShouldBeAbleToCreateADriverWithValidValues() throws Exception {
        final var expectedName = "John";
        final var aRequestInput = new CreateDriverRequest(expectedName);

        Assertions.assertEquals(0, driverRepository.count());

        final var request =
                MockMvcRequestBuilders.post(BASE_PATH)
                        .content(Json.writeValueAsString(aRequestInput))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);


        final var result = this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(expectedName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        final var actualResponseBody = Json.readValue(result.getResponse().getContentAsString(), CreateDriverResponse.class);

        Assertions.assertEquals(1, driverRepository.count());
        final var actualEntity = driverRepository.findById(actualResponseBody.id()).get();

        Assertions.assertNotNull(actualEntity.getId());
        Assertions.assertEquals(actualEntity.getName(), expectedName);
        Assertions.assertNotNull(actualEntity.getUpdatedAt());
        Assertions.assertNotNull(actualEntity.getCreatedAt());
        Assertions.assertEquals(actualEntity.getCreatedAt(), actualEntity.getUpdatedAt());

        Assertions.assertEquals(actualResponseBody.id(), actualEntity.getId());
        Assertions.assertEquals(actualResponseBody.name(), actualEntity.getName());
        Assertions.assertEquals(actualResponseBody.updatedAt(), actualEntity.getUpdatedAt());
        Assertions.assertEquals(actualResponseBody.createdAt(), actualEntity.getCreatedAt());
    }

    @Test
    public void asAUser_IShouldBeAbleToSeeTreatedErrorOnCreateDriverWithEmptyName() throws Exception {
        final var expectedName = "  ";

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aRequestInput = new CreateDriverRequest(expectedName);

        Assertions.assertEquals(0, driverRepository.count());

        final var request =
                MockMvcRequestBuilders.post(BASE_PATH)
                        .content(Json.writeValueAsString(aRequestInput))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);


        this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(expectedErrorCount)))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        Assertions.assertEquals(0, driverRepository.count());
    }

    @Test
    public void asAUser_IShouldBeAbleToSeeTreatedErrorOnCreateDriverWithNullName() throws Exception {
        final String expectedName = null;

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aRequestInput = new CreateDriverRequest(expectedName);

        Assertions.assertEquals(0, driverRepository.count());

        final var request =
                MockMvcRequestBuilders.post(BASE_PATH)
                        .content(Json.writeValueAsString(aRequestInput))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);


        this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(expectedErrorCount)))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        Assertions.assertEquals(0, driverRepository.count());
    }

    @Test
    public void asAUser_IShouldBeAbleToSeeTreatedErrorOnCreateDriverWithNameMoreThan256Characters() throws Exception {
        final String expectedName = """
                O empenho em analisar a constante divulgação das informações faz parte 
                de um processo de gerenciamento das posturas dos órgãos dirigentes com
                 relação às suas atribuições. Do mesmo modo, o novo modelo estrutural aqui
                  preconizado nos obriga à análise dos modos de operação convencionais. 
                  Pensando mais a longo prazo, o desenvolvimento contínuo de distintas 
                  formas de atuação pode nos levar a considerar a reestruturação das 
                  direções preferenciais no sentido do progresso. A nível organizacional,
                   o surgimento do comércio virtual facilita a criação do remanejamento
                    dos quadros funcionais. Gostaria de enfatizar que o comprometimento 
                    entre as equipes obstaculiza a apreciação da importância das diretrizes 
                    de desenvolvimento para o futuro.
                   """;

        final var expectedErrorMessage = "'name' should be between 1 and 255 characters";
        final var expectedErrorCount = 1;

        final var aRequestInput = new CreateDriverRequest(expectedName);

        Assertions.assertEquals(0, driverRepository.count());

        final var request =
                MockMvcRequestBuilders.post(BASE_PATH)
                        .content(Json.writeValueAsString(aRequestInput))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);


        this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(expectedErrorCount)))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        Assertions.assertEquals(0, driverRepository.count());
    }

    @Test
    public void asAUser_IShouldBeAbleToUpdateAExistentDriverSuccessfully() throws Exception {
        final var aDriver = Driver.newDriver("jo");

        final var expectedName = "John";
        final var expectedId = aDriver.getId();

        Assertions.assertEquals(0, driverRepository.count());
        driverRepository.save(DriverJpaEntity.from(aDriver));
        Assertions.assertEquals(1, driverRepository.count());


        final var aRequestInput = new UpdateDriverRequest(expectedId.getValue(), expectedName);

        final var request =
                MockMvcRequestBuilders.put(BASE_PATH + "/{id}", expectedId.getValue())
                        .content(Json.writeValueAsString(aRequestInput))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        final var actualEntity = driverRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedId.getValue(), actualEntity.getId());
        Assertions.assertEquals(actualEntity.getName(), expectedName);
        Assertions.assertNotNull(actualEntity.getUpdatedAt());
        Assertions.assertNotNull(actualEntity.getCreatedAt());
        Assertions.assertTrue(actualEntity.getCreatedAt().isBefore(actualEntity.getUpdatedAt()));

    }

    @Test
    public void asAUser_IShouldBeAbleToSeeATreatedErrorOnNameIsEmpty() throws Exception {
        final var aDriver = Driver.newDriver("jo");

        final var expectedName = aDriver.getName();
        final var expectedId = aDriver.getId();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        Assertions.assertEquals(0, driverRepository.count());
        driverRepository.save(DriverJpaEntity.from(aDriver));
        Assertions.assertEquals(1, driverRepository.count());


        final var aRequestInput = new UpdateDriverRequest(expectedId.getValue(), "  ");

        final var request =
                MockMvcRequestBuilders.put(BASE_PATH + "/{id}", expectedId.getValue())
                        .content(Json.writeValueAsString(aRequestInput))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(expectedErrorCount)))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        final var actualEntity = driverRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedId.getValue(), actualEntity.getId());
        Assertions.assertEquals(actualEntity.getName(), expectedName);
        Assertions.assertNotNull(actualEntity.getUpdatedAt());
        Assertions.assertNotNull(actualEntity.getCreatedAt());
        Assertions.assertEquals(actualEntity.getCreatedAt(), actualEntity.getUpdatedAt());

    }

    @Test
    public void asAUser_IShouldBeAbleToSeeATreatedErrorOnNameIsNull() throws Exception {
        final var aDriver = Driver.newDriver("jo");

        final var expectedName = aDriver.getName();
        final var expectedId = aDriver.getId();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        Assertions.assertEquals(0, driverRepository.count());
        driverRepository.save(DriverJpaEntity.from(aDriver));
        Assertions.assertEquals(1, driverRepository.count());


        final var aRequestInput = new UpdateDriverRequest(expectedId.getValue(), null);

        final var request =
                MockMvcRequestBuilders.put(BASE_PATH + "/{id}", expectedId.getValue())
                        .content(Json.writeValueAsString(aRequestInput))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(expectedErrorCount)))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        final var actualEntity = driverRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedId.getValue(), actualEntity.getId());
        Assertions.assertEquals(actualEntity.getName(), expectedName);
        Assertions.assertNotNull(actualEntity.getUpdatedAt());
        Assertions.assertNotNull(actualEntity.getCreatedAt());
        Assertions.assertEquals(actualEntity.getCreatedAt(), actualEntity.getUpdatedAt());

    }

    @Test
    public void asAUser_IShouldBeAbleToSeeATreatedErrorOnNameIsThan256Characters() throws Exception {
        final String aName = """
                O empenho em analisar a constante divulgação das informações faz parte 
                de um processo de gerenciamento das posturas dos órgãos dirigentes com
                 relação às suas atribuições. Do mesmo modo, o novo modelo estrutural aqui
                  preconizado nos obriga à análise dos modos de operação convencionais. 
                  Pensando mais a longo prazo, o desenvolvimento contínuo de distintas 
                  formas de atuação pode nos levar a considerar a reestruturação das 
                  direções preferenciais no sentido do progresso. A nível organizacional,
                   o surgimento do comércio virtual facilita a criação do remanejamento
                    dos quadros funcionais. Gostaria de enfatizar que o comprometimento 
                    entre as equipes obstaculiza a apreciação da importância das diretrizes 
                    de desenvolvimento para o futuro.
                   """;

        final var aDriver = Driver.newDriver("jo");

        final var expectedName = aDriver.getName();
        final var expectedId = aDriver.getId();

        final var expectedErrorMessage = "'name' should be between 1 and 255 characters";
        final var expectedErrorCount = 1;

        Assertions.assertEquals(0, driverRepository.count());
        driverRepository.save(DriverJpaEntity.from(aDriver));
        Assertions.assertEquals(1, driverRepository.count());


        final var aRequestInput = new UpdateDriverRequest(expectedId.getValue(), aName);

        final var request =
                MockMvcRequestBuilders.put(BASE_PATH + "/{id}", expectedId.getValue())
                        .content(Json.writeValueAsString(aRequestInput))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(expectedErrorCount)))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        final var actualEntity = driverRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedId.getValue(), actualEntity.getId());
        Assertions.assertEquals(actualEntity.getName(), expectedName);
        Assertions.assertNotNull(actualEntity.getUpdatedAt());
        Assertions.assertNotNull(actualEntity.getCreatedAt());
        Assertions.assertEquals(actualEntity.getCreatedAt(), actualEntity.getUpdatedAt());

    }

    @Test
    public void asAUser_IShouldBeAbleToFindAExistentDriverSuccessfully() throws Exception {
        final var aDriver = Driver.newDriver("John");
        final var expectedName = "John";
        final var expectedId = aDriver.getId();
        final var expectedCreatedAt = aDriver.getCreatedAt();
        final var expectedUpdatedAt = aDriver.getUpdatedAt();

        Assertions.assertEquals(0, driverRepository.count());
        driverRepository.save(DriverJpaEntity.from(aDriver));
        Assertions.assertEquals(1, driverRepository.count());


        final var request =
                MockMvcRequestBuilders.get(BASE_PATH + "/{id}", expectedId.getValue())
                        .accept(MediaType.APPLICATION_JSON);

        final var result = this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        final var actualResponseBody = Json.readValue(result.getResponse().getContentAsString(), FindDriverByIdResponse.class);

        Assertions.assertEquals(expectedId.getValue(), actualResponseBody.id());
        Assertions.assertEquals(actualResponseBody.name(), expectedName);
        Assertions.assertEquals(actualResponseBody.updatedAt(), expectedUpdatedAt);
        Assertions.assertEquals(actualResponseBody.createdAt(), expectedCreatedAt);

        final var actualEntity = driverRepository.findById(expectedId.getValue()).get();
        Assertions.assertEquals(expectedId.getValue(), actualEntity.getId());
        Assertions.assertEquals(actualEntity.getName(), expectedName);
        Assertions.assertEquals(actualEntity.getUpdatedAt(), expectedUpdatedAt);
        Assertions.assertEquals(actualEntity.getCreatedAt(), expectedCreatedAt);

    }

    @Test
    public void asAUser_IShouldBeAbleToSeeATreatedNotFoundErrorWhenIdNotExists() throws Exception {
        final var expectedId = DriverID.from("123");

        final var expectedErrorMessage = "Driver with identifier 123 was not found";
        final var expectedErrorCount = 0;

        Assertions.assertEquals(0, driverRepository.count());

        final var request =
                MockMvcRequestBuilders.get(BASE_PATH + "/{id}", expectedId.getValue())
                        .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(expectedErrorCount)))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE));

        Assertions.assertEquals(0, driverRepository.count());
    }

    @Test
    public void asAUser_IShouldBeAbleToRemoveAExistentDriverSuccessfully() throws Exception {
        final var aDriver = Driver.newDriver("John");
        final var expectedId = aDriver.getId();

        Assertions.assertEquals(0, driverRepository.count());
        driverRepository.save(DriverJpaEntity.from(aDriver));
        Assertions.assertEquals(1, driverRepository.count());


        final var request =
                MockMvcRequestBuilders.delete(BASE_PATH + "/{id}", expectedId.getValue());

        this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn();

        final var optionalEntity = driverRepository.findById(expectedId.getValue());

        Assertions.assertTrue(optionalEntity.isEmpty());
        Assertions.assertEquals(0, driverRepository.count());

    }

    @Test
    public void asAUser_IShouldBeAbleRemoveWithoutExistingDriver() throws Exception {
        final var expectedId = DriverID.from("123");

        Assertions.assertEquals(0, driverRepository.count());


        final var request =
                MockMvcRequestBuilders.delete(BASE_PATH + "/{id}", expectedId.getValue());

        this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn();

        final var optionalEntity = driverRepository.findById(expectedId.getValue());

        Assertions.assertTrue(optionalEntity.isEmpty());
        Assertions.assertEquals(0, driverRepository.count());

    }


    @Test
    public void asAUser_IShouldBeAbleToFindAllExistentDriversSuccessfully() throws Exception {
        final var drivers = List.of(
                Driver.newDriver("John"),
                Driver.newDriver("Mel"),
                Driver.newDriver("Mohamed")
        );

        final var expectedDriversCount = 3;

        Assertions.assertEquals(0, driverRepository.count());
        driverRepository.saveAllAndFlush(mapTo(drivers, DriverJpaEntity::from));
        Assertions.assertEquals(expectedDriversCount, driverRepository.count());


        final var request =
                MockMvcRequestBuilders.get(BASE_PATH)
                        .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(expectedDriversCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.equalTo(drivers.get(0).getId().getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.equalTo(drivers.get(0).getName())))

                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.equalTo(drivers.get(1).getId().getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.equalTo(drivers.get(1).getName())))

                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id", Matchers.equalTo(drivers.get(2).getId().getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name", Matchers.equalTo(drivers.get(2).getName())))
                .andReturn();
    }

    @Test
    public void asAUser_IShouldBeAbleToSeeAEmptyListWhenDriversNotExists() throws Exception {
        final var expectedDriversCount = 0;

        Assertions.assertEquals(0, driverRepository.count());


        final var request =
                MockMvcRequestBuilders.get(BASE_PATH)
                        .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(expectedDriversCount)))
                .andReturn();
    }

    private <I, O> List<O> mapTo(List<I> list, Function<I, O> mapper) {
        return list.stream().map(mapper).toList();
    }


}
