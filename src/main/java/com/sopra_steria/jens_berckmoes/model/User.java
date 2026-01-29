package com.sopra_steria.jens_berckmoes.model;

public record User(String username, String token) {
    public static User of(String validUsername, String someValidToken) {
        return new User(validUsername, someValidToken);
    }
}
