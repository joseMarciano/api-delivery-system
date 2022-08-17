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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@E2ETest
public class DriverControllerTest {

    private static final String BASE_PATH = "/drivers";

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
    public void testando2() {
//        Assertions.assertEquals(0, driverRepository.count());
//        driverRepository.save(DriverJpaEntity.from(Driver.newDriver("John")));
//        Assertions.assertEquals(1, driverRepository.count());
    }
}
