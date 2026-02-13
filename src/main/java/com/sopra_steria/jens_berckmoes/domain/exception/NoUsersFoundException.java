package com.sopra_steria.jens_berckmoes.domain.exception;

public class NoUsersFoundException extends RuntimeException{
    public NoUsersFoundException(final String message) {
        super(message);
    }
}
