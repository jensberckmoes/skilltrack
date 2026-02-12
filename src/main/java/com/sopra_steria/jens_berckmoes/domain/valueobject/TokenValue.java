package com.sopra_steria.jens_berckmoes.domain.valueobject;

import com.sopra_steria.jens_berckmoes.domain.exception.TokenRawValueNullOrBlankException;

public record TokenValue(String value) {

    public TokenValue {
        if (value == null) {
            throw new TokenRawValueNullOrBlankException("Token can not be null");
        }
        if (value.isBlank()) {
            throw new TokenRawValueNullOrBlankException("Token can not be blank");
        }
    }

    public static TokenValue of(final String value) throws IllegalArgumentException {
        return new TokenValue(value);
    }

}
