package com.sopra_steria.jens_berckmoes.bdd.fakes;

import java.util.concurrent.Callable;

public record StepResult<T>(T body, String exceptionMessage) {

    public static <T> StepResult<T> success(final T body) {
        return new StepResult<>(body, null);
    }

    public static <T> StepResult<T> failure(final String message) {
        return new StepResult<>(null,  message);
    }

    public boolean isSuccess() {
        return exceptionMessage == null;
    }

    public static <T> StepResult<T> callController(final Callable<T> action) {
        try {
            return success(action.call());
        } catch (final Exception e) {
            return failure(e.getMessage());
        }
    }
}





