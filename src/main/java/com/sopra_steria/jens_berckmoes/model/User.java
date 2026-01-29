package com.sopra_steria.jens_berckmoes.model;

public record User(String username, Token token) {
    public static User of(final Username username, final Token token) {
        return new User(username.value(), token);
    }

    public static User defaultUser(final Username username){
        return of(username, Token.weeklyToken("some_valid_token"));
    }
}
