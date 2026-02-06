package com.sopra_steria.jens_berckmoes.domain;

import com.sopra_steria.jens_berckmoes.domain.exception.TokenRawValueNullOrBlankException;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenExpirationDateNullException;

import java.time.LocalDate;

public record Token(String token, LocalDate expirationDate) {
    public static Token of(final String token, final LocalDate expirationDate) {
        if (token == null || token.isBlank()) {
            throw new TokenRawValueNullOrBlankException();
        }
        if (expirationDate == null) {
            throw new TokenExpirationDateNullException();
        }
        return new Token(token, expirationDate);
    }

    public boolean isExpiredAt(final LocalDate referenceDate) {
        return expirationDate.isBefore(referenceDate);
    }
}
