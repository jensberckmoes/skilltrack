package com.sopra_steria.jens_berckmoes.model;

public record User(String username, Token token) {
    public static User of(final Username username, final Token token) {
        return new User(username.value(), token);
    }

    public boolean ownsToken(final Token token){
        return this.token.equals(token);
    }
}
