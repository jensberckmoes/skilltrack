package com.sopra_steria.jens_berckmoes.BDD.steps;

public record Token(String token) {

    public static Token of(final String validToken) {
        return new Token(validToken);
    }
}
