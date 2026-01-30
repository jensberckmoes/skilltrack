package com.sopra_steria.jens_berckmoes.service;

import com.sopra_steria.jens_berckmoes.exception.TokenDoesNotBelongToUserException;
import com.sopra_steria.jens_berckmoes.exception.TokenHasExpiredException;
import com.sopra_steria.jens_berckmoes.exception.TokenNotFoundException;
import com.sopra_steria.jens_berckmoes.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.model.*;
import com.sopra_steria.jens_berckmoes.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.repository.UserRepository;

import static java.time.LocalDateTime.now;

public record LoginService(UserRepository userRepository, TokenRepository tokenRepository) {
    public LoginResult login(final Username username, final TokenValue tokenValue) {
        try {
            final User user = userRepository.get(username);
            final Token token = tokenRepository.get(tokenValue);
            if (!token.belongsTo(user)) {
                throw new TokenDoesNotBelongToUserException();
            }
            if (token.isExpiredAt(now())) {
                throw new TokenHasExpiredException();
            }
            return LoginResult.success();
        } catch (final UserNotFoundException | TokenNotFoundException | TokenDoesNotBelongToUserException |
                       TokenHasExpiredException e) {
            return LoginResult.of(LoginStatus.BLOCKED, "please contact support");
        }
    }
}
