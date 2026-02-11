package com.sopra_steria.jens_berckmoes.domain.exception;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(final String message) {
        super(message);
    }
}
