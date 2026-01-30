package com.sopra_steria.jens_berckmoes.model;

import com.sopra_steria.jens_berckmoes.exception.TokenRawValueNullOrBlankException;

public record TokenValue(String value) {
    public static TokenValue of(final String token) throws IllegalArgumentException {
        if (token == null || token.isBlank()) {
            throw new TokenRawValueNullOrBlankException();
        }
        return new TokenValue(token);
    }

}
