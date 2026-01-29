package com.sopra_steria.jens_berckmoes.BDD.steps;

public record LoginResult(LoginStatus loginStatus, String errorMessage) {
    public static LoginResult of(LoginStatus loginStatus, String message) {
        return new LoginResult(loginStatus, message);
    }
}
