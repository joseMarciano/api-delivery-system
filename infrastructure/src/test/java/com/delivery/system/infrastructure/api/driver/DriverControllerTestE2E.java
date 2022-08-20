package com.delivery.system.infrastructure.api.driver;

import com.delivery.system.E2ETest;
import com.delivery.system.configs.json.Json;
import com.delivery.system.infrastructure.driver.models.create.CreateDriverRequest;
import com.delivery.system.infrastructure.driver.models.create.CreateDriverResponse;
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
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

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


    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresDB::getJdbcUrl);
        registry.add("spring.datasource.username", postgresDB::getUsername);
        registry.add("spring.datasource.password", postgresDB::getPassword);
        registry.add("spring.datasource.port", () -> postgresDB.getMappedPort(5432));
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
}
