package com.sopra_steria.jens_berckmoes.domain;

import com.sopra_steria.jens_berckmoes.domain.exception.TokenRawValueNullOrBlankException;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenValidUntilNullException;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

public record Token(String token, LocalDateTime expirationDate) {

    public static Token weeklyToken(final String token) {
        return of(token, now().plusDays(7));
    }

    public static Token of(final String token, final LocalDateTime validUntil) {
        if (token == null || token.isBlank()) {
            throw new TokenRawValueNullOrBlankException();
        }
        if (validUntil == null) {
            throw new TokenValidUntilNullException();
        }
        return new Token(token, validUntil);
    }

    public boolean isExpiredAt(final LocalDateTime referenceTime) {
        return expirationDate.isBefore(referenceTime);
    }
}
