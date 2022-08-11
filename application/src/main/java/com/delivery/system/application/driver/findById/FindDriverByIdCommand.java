package com.delivery.system.application.driver.findById;

public record FindDriverByIdCommand(String anId) {


    public static FindDriverByIdCommand with(final String anId){
        return new FindDriverByIdCommand(anId);
    }
}
