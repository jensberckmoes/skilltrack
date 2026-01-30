package com.sopra_steria.jens_berckmoes.model;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

public record Token(String token, LocalDateTime validUntil) {

    public static Token weeklyToken(final String token) {
        return of(token, now().plusDays(7));
    }

    public static Token of(final String token, final LocalDateTime validUntil) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token value cannot be null or blank");
        }
        if (validUntil == null) {
            throw new IllegalArgumentException("Token validUntil cannot be null");
        }
        return new Token(token, validUntil);
    }

    public boolean hasExpired() {
        return validUntil.isBefore(now());
    }

    public boolean belongsTo(final User user) {
        return user.token().equals(this);
    }
}
