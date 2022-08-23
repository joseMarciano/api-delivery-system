package com.delivery.system.infrastructure.api;

import com.delivery.system.infrastructure.driver.models.create.CreateDriverRequest;
import com.delivery.system.infrastructure.driver.models.create.CreateDriverResponse;
import com.delivery.system.infrastructure.driver.models.findAll.FindAllDriverResponse;
import com.delivery.system.infrastructure.driver.models.findById.FindDriverByIdResponse;
import com.delivery.system.infrastructure.driver.models.update.UpdateDriverRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/drivers")
public interface DriverAPI {

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    CreateDriverResponse create(@RequestBody CreateDriverRequest aDriverRequest);


    @PutMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void update(@RequestBody UpdateDriverRequest aDriverRequest);

    @GetMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<FindDriverByIdResponse> findById(@PathVariable("id") String anId);

    @DeleteMapping(
            value = "{id}"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void update(@PathVariable("id") String anId);


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    List<FindAllDriverResponse> findAll();
}

