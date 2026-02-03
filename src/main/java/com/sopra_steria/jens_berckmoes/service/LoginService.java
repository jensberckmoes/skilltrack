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

import java.time.LocalDate;

@Service
public record LoginService(UserRepository userRepository, TokenRepository tokenRepository) {
    public LoginResult login(final Username username, final TokenValue tokenValue) {
        return loginAt(username, tokenValue, LocalDate.now());
    }

    private LoginResult loginAt(final Username username, final TokenValue tokenValue, final LocalDate now) {
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

    private static void ensureTokenNotExpired(final Token token, final LocalDate now) {
        if (token.isExpiredAt(now)) {
            throw new TokenHasExpiredException();
        }
    }
}
