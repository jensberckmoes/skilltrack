package com.sopra_steria.jens_berckmoes.BDD.steps;

public class LoginService {
    public LoginResult login(String validUsername, Token validToken) {
        return LoginResult.of(LoginStatus.Blocked, "please contact support");
    }
}
