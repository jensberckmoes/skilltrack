package com.sopra_steria.jens_berckmoes.service;

import com.sopra_steria.jens_berckmoes.domain.LoginResult;
import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenDoesNotBelongToUserException;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenHasExpiredException;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenNotFoundException;
import com.sopra_steria.jens_berckmoes.domain.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.domain.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.domain.repository.UserRepository;
import com.sopra_steria.jens_berckmoes.domain.valueobject.TokenValue;
import com.sopra_steria.jens_berckmoes.domain.valueobject.Username;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public record ValidateAssessmentAccessService(UserRepository userRepository, TokenRepository tokenRepository, Clock clock) {

    public LoginResult login(final Username username, final TokenValue tokenValue) {
        return loginAt(username, tokenValue);
    }

    private LoginResult loginAt(final Username username, final TokenValue tokenValue) {
        try {
            final User user = userRepository.findByUsername(username);
            final Token token = tokenRepository.findByTokenValue(tokenValue);
            ensureTokenBelongsToUser(token, user);
            ensureTokenNotExpired(token);
            return LoginResult.success();
        } catch(final UserNotFoundException |
                      TokenNotFoundException |
                      TokenDoesNotBelongToUserException |
                      TokenHasExpiredException e) {
            return LoginResult.blocked();
        }
    }

    private void ensureTokenBelongsToUser(final Token token, final User user) {
        if(!user.ownsToken(token)) {
            throw new TokenDoesNotBelongToUserException();
        }
    }

    private void ensureTokenNotExpired(final Token token) {
        if(token.hasExpired(clock)) {
            throw new TokenHasExpiredException();
        }
    }
}
