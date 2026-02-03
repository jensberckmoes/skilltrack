package com.sopra_steria.jens_berckmoes.domain.valueobject;

import com.sopra_steria.jens_berckmoes.domain.exception.TokenRawValueNullOrBlankException;

public record TokenValue(String value) {
    public static TokenValue of(final String token) throws IllegalArgumentException {
        if (token == null || token.isBlank()) {
            throw new TokenRawValueNullOrBlankException();
        }
        return new TokenValue(token);
    }

}
