package com.sopra_steria.jens_berckmoes.model;

public record User(String username, Token token) {
    public static User of(final Username username, final Token token) {
        return new User(username.value(), token);
    }
}
