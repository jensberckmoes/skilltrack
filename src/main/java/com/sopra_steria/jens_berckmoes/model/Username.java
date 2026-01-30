package com.sopra_steria.jens_berckmoes.model;

public record Username(String value) {
    public static Username of(final String raw) throws IllegalArgumentException{
        if(raw == null || raw.isBlank()){
            throw new IllegalArgumentException("Invalid username");
        }
        return new Username(raw);
    }
}
