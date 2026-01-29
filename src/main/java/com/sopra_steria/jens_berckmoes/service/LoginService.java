package com.sopra_steria.jens_berckmoes.service;

import com.sopra_steria.jens_berckmoes.model.LoginResult;
import com.sopra_steria.jens_berckmoes.model.LoginStatus;
import com.sopra_steria.jens_berckmoes.model.Token;

public class LoginService {
    public LoginResult login(String validUsername, Token validToken) {
        return LoginResult.of(LoginStatus.Blocked, "please contact support");
    }
}
