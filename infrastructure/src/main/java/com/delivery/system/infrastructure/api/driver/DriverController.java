package com.delivery.system.infrastructure.api.driver;

import com.delivery.system.application.driver.create.CreateDriverCommand;
import com.delivery.system.application.driver.create.CreateDriverUseCase;
import com.delivery.system.application.driver.findById.FindDriverByIdCommand;
import com.delivery.system.application.driver.findById.FindDriverByIdUseCase;
import com.delivery.system.application.driver.update.UpdateDriverUseCase;
import com.delivery.system.infrastructure.api.DriverAPI;
import com.delivery.system.infrastructure.driver.models.create.CreateDriverRequest;
import com.delivery.system.infrastructure.driver.models.create.CreateDriverResponse;
import com.delivery.system.infrastructure.driver.models.findById.FindDriverByIdResponse;
import com.delivery.system.infrastructure.driver.models.update.UpdateDriverRequest;
import com.delivery.system.infrastructure.driver.presenters.DriverPresenters;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DriverController implements DriverAPI {

    private final CreateDriverUseCase createDriverUseCase;
    private final UpdateDriverUseCase updateDriverUseCase;
    private final FindDriverByIdUseCase findDriverByIdUseCase;

    public DriverController(CreateDriverUseCase createDriverUseCase,
                            UpdateDriverUseCase updateDriverUseCase,
                            FindDriverByIdUseCase findDriverByIdUseCase) {
        this.createDriverUseCase = createDriverUseCase;
        this.updateDriverUseCase = updateDriverUseCase;
        this.findDriverByIdUseCase = findDriverByIdUseCase;
    }

    @Override
    public CreateDriverResponse create(CreateDriverRequest aDriverRequest) {
        final var aDriver = createDriverUseCase.execute(CreateDriverCommand.with(aDriverRequest.name()));
        return DriverPresenters.present(aDriver);
    }

    @Override
    public void update(UpdateDriverRequest aDriverRequest) {
        updateDriverUseCase.execute(UpdateDriverRequest.with(aDriverRequest));
    }

    @Override
    public ResponseEntity<FindDriverByIdResponse> findById(String anId) {
        final var anOutput = findDriverByIdUseCase.execute(FindDriverByIdCommand.with(anId));
        return ResponseEntity.ok(DriverPresenters.present(anOutput));
    }
}
