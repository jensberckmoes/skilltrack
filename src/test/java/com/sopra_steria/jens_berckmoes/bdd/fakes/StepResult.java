package com.sopra_steria.jens_berckmoes.bdd.fakes;

import java.util.Set;
import java.util.concurrent.Callable;

public record StepResult<T>(T body, Exception exception) {

    public static <T> StepResult<T> success(final T body) {
        return new StepResult<>(body, null);
    }

    public static <T> StepResult<T> failure(final Exception exception) {
        return new StepResult<>(null,  exception);
    }

    public boolean isSuccess() {
        return exception == null;
    }

    public boolean isFailure() {
        return !isSuccess();
    }

    public static <T> StepResult<T> callController(final Callable<T> action) {
        try {
            return success(action.call());
        } catch (final Exception e) {
            return failure(e);
        }
    }
}





