package com.sopra_steria.jens_berckmoes.service;

import com.sopra_steria.jens_berckmoes.model.LoginResult;
import com.sopra_steria.jens_berckmoes.model.LoginStatus;
import com.sopra_steria.jens_berckmoes.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.repository.UserRepository;

public record LoginService(UserRepository userRepository, TokenRepository tokenRepository) {
    public LoginResult login(final String validUsername, final String validToken) {
        if (validUsername == null || validToken == null || validUsername.isBlank() || validToken.isBlank() || !userRepository.exists(validUsername) || !tokenRepository.exists(validToken)) {
            return LoginResult.of(LoginStatus.BLOCKED, "please contact support");
        }
        return LoginResult.of(LoginStatus.SUCCESS, "");
    }
}
