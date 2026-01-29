package com.sopra_steria.jens_berckmoes.model;

public record Token(String token) {

    public static Token of(final String validToken) {
        return new Token(validToken);
    }
}
