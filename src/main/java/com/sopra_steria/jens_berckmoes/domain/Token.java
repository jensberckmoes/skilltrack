package com.sopra_steria.jens_berckmoes.domain;

import java.time.Clock;
import java.time.LocalDate;

public record Token(String token, LocalDate expirationDate) {
    public static final Token NULL = new Token("wrong_token", LocalDate.MIN);

    public static Token of(final String token, final LocalDate expirationDate) {
        return new Token(token, expirationDate);
    }

    public boolean hasExpired(final Clock clock) {
        return expirationDate.isBefore(LocalDate.now(clock));
    }
}
