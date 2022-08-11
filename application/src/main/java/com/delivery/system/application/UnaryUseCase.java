package com.delivery.system.application;

public abstract class UnaryUseCase<IN> {
    public abstract void execute(IN anIn);
}
