package com.sopra_steria.jens_berckmoes.service;

import com.sopra_steria.jens_berckmoes.exception.TokenDoesNotBelongToUserException;
import com.sopra_steria.jens_berckmoes.exception.TokenHasExpiredException;
import com.sopra_steria.jens_berckmoes.exception.TokenNotFoundException;
import com.sopra_steria.jens_berckmoes.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.model.*;
import com.sopra_steria.jens_berckmoes.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public record LoginService(UserRepository userRepository, TokenRepository tokenRepository) {
    public LoginResult login(final Username username, final TokenValue tokenValue) {
        return loginAt(username, tokenValue, LocalDateTime.now());
    }

    private LoginResult loginAt(final Username username, final TokenValue tokenValue, final LocalDateTime now) {
        try {
            final User user = userRepository.get(username);
            final Token token = tokenRepository.get(tokenValue);
            ensureTokenBelongsToUser(token, user);
            ensureTokenNotExpired(token, now);
            return LoginResult.success();
        } catch (final UserNotFoundException | TokenNotFoundException | TokenDoesNotBelongToUserException |
                       TokenHasExpiredException e) {
            return LoginResult.blocked();
        }
    }

    private static void ensureTokenBelongsToUser(final Token token, final User user) {
        if (!user.ownsToken(token)) {
            throw new TokenDoesNotBelongToUserException();
        }
    }

    private static void ensureTokenNotExpired(final Token token, final LocalDateTime now) {
        if (token.isExpiredAt(now)) {
            throw new TokenHasExpiredException();
        }
    }
}
