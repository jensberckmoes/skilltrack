package com.sopra_steria.jens_berckmoes.model;

public record LoginResult(LoginStatus loginStatus, String errorMessage) {
    public static LoginResult of(LoginStatus loginStatus, String message) {
        return new LoginResult(loginStatus, message);
    }
}
