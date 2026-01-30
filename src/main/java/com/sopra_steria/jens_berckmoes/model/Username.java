package com.sopra_steria.jens_berckmoes.model;

import com.sopra_steria.jens_berckmoes.exception.UsernameRawValueNullOrBlankException;

public record Username(String value) {
    public static Username of(final String raw) throws IllegalArgumentException{
        if(raw == null || raw.isBlank()){
            throw new UsernameRawValueNullOrBlankException();
        }
        return new Username(raw);
    }
}
