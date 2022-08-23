package com.delivery.system.infrastructure.driver.models.update;

import com.delivery.system.application.driver.update.UpdateDriverCommand;

public record UpdateDriverRequest(String id, String name) {

    public static UpdateDriverCommand with(final UpdateDriverRequest aDriverRequest) {
        return UpdateDriverCommand.with(aDriverRequest.id(), aDriverRequest.name());
    }

}
