package com.sopra_steria.jens_berckmoes.domain.valueobject;

import com.sopra_steria.jens_berckmoes.domain.exception.UsernameRawValueNullOrBlankException;

public record Username(String value) {

    public Username {
        if(value == null) {
            throw new UsernameRawValueNullOrBlankException("Username can not be null");
        }
        if(value.isBlank()) {
            throw new UsernameRawValueNullOrBlankException("Username can not be blank");
        }
    }

    public static Username of(final String value) throws UsernameRawValueNullOrBlankException {
        return new Username(value);
    }
}
