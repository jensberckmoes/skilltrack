package com.sopra_steria.jens_berckmoes.domain;

public record User(String username, Token token) {
    public static User of(final String username, final Token token) {
        return new User(username, token);
    }

    public boolean ownsToken(final Token token) {
        return this.token.equals(token);
    }
}
