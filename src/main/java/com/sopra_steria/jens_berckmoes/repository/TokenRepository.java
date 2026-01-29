package com.sopra_steria.jens_berckmoes.repository;

import com.sopra_steria.jens_berckmoes.exception.TokenNotFoundException;
import com.sopra_steria.jens_berckmoes.model.Token;
import com.sopra_steria.jens_berckmoes.model.TokenValue;

import java.time.LocalDateTime;

public class TokenRepository {
    public Token get(final TokenValue token) throws TokenNotFoundException {
        try {
            return Token.of(token.value(), LocalDateTime.now().plusDays(5));
        } catch (final IllegalArgumentException e) {
            throw new TokenNotFoundException();
        }
    }
}
