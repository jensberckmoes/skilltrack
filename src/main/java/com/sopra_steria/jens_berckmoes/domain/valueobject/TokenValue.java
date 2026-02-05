package com.sopra_steria.jens_berckmoes.domain.valueobject;

import com.sopra_steria.jens_berckmoes.domain.exception.TokenRawValueNullOrBlankException;

public record TokenValue(String value) {

    public TokenValue {
        if (value == null || value.isBlank()) {
            throw new TokenRawValueNullOrBlankException();
        }
    }

    public static TokenValue of(final String value) throws IllegalArgumentException {
        return new TokenValue(value);
    }

}
