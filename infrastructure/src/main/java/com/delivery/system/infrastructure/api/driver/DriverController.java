package com.delivery.system.infrastructure.api.driver;

import com.delivery.system.application.driver.create.CreateDriverCommand;
import com.delivery.system.application.driver.create.CreateDriverUseCase;
import com.delivery.system.application.driver.update.UpdateDriverUseCase;
import com.delivery.system.infrastructure.api.DriverAPI;
import com.delivery.system.infrastructure.driver.models.create.CreateDriverRequest;
import com.delivery.system.infrastructure.driver.models.create.CreateDriverResponse;
import com.delivery.system.infrastructure.driver.models.update.UpdateDriverRequest;
import com.delivery.system.infrastructure.driver.presenters.DriverPresenters;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DriverController implements DriverAPI {

    private final CreateDriverUseCase createDriverUseCase;
    private final UpdateDriverUseCase updateDriverUseCase;

    public DriverController(CreateDriverUseCase createDriverUseCase, UpdateDriverUseCase updateDriverUseCase) {
        this.createDriverUseCase = createDriverUseCase;
        this.updateDriverUseCase = updateDriverUseCase;
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
}
