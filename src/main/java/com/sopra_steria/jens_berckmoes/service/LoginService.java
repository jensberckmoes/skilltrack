package com.sopra_steria.jens_berckmoes.service;

import com.sopra_steria.jens_berckmoes.model.*;
import com.sopra_steria.jens_berckmoes.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.repository.UserRepository;

public record LoginService(UserRepository userRepository, TokenRepository tokenRepository) {
    public LoginResult login(final Username username, final TokenValue tokenValue) {
        try {
            final User user = userRepository.get(username);
            final Token token = tokenRepository.get(tokenValue);
            if(!token.belongsTo(user)){
                throw new IllegalArgumentException("Token is of a different User");
            }
        }catch (final RuntimeException e) {
            return LoginResult.of(LoginStatus.BLOCKED, "please contact support");
        }
        return LoginResult.of(LoginStatus.SUCCESS, "");
    }
}
