package com.sopra_steria.jens_berckmoes.model;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

public record Token(String token, LocalDateTime validUntil) {

    public static Token of(final String validToken) {
        return of(validToken, now());
    }

    public static Token of(final String validToken, final LocalDateTime validUntil) {
        return new Token(validToken, validUntil);
    }
}
