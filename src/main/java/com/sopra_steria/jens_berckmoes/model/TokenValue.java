package com.sopra_steria.jens_berckmoes.model;

public record TokenValue(String value) {
    public static TokenValue of(final String token) throws IllegalArgumentException {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Invalid token");
        }
        return new TokenValue(token);
    }

}
