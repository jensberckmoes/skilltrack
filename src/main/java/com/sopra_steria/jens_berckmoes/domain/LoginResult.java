package com.sopra_steria.jens_berckmoes.domain;

public record LoginResult(LoginStatus loginStatus) {

    public static LoginResult success() {
        return new LoginResult(LoginStatus.SUCCESS);
    }

    public static LoginResult blocked() {
        return new LoginResult(LoginStatus.BLOCKED);
    }
}
