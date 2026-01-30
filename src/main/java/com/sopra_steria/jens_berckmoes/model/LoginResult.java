package com.sopra_steria.jens_berckmoes.model;

public record LoginResult(LoginStatus loginStatus, String errorMessage) {
    public static LoginResult of(LoginStatus loginStatus, String message) {
        return new LoginResult(loginStatus, message);
    }

    public static LoginResult success() {
        return of(LoginStatus.SUCCESS, "");
    }

    public static LoginResult blocked() {
        return of(LoginStatus.BLOCKED, "please contact support");
    }
}
