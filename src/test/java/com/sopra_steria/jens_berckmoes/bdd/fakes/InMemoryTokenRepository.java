package com.sopra_steria.jens_berckmoes.bdd.fakes;

import com.sopra_steria.jens_berckmoes.exception.TokenNotFoundException;
import com.sopra_steria.jens_berckmoes.model.Token;
import com.sopra_steria.jens_berckmoes.model.TokenValue;
import com.sopra_steria.jens_berckmoes.repository.TokenRepository;

import java.time.LocalDateTime;
import java.util.Map;

public class InMemoryTokenRepository implements TokenRepository {
    private static final LocalDateTime STATIC_NOW = LocalDateTime.of(2026, 1, 30, 12, 15, 0);
    private final Map<TokenValue, Token> tokens = construct();

    private Map<TokenValue, Token> construct() {
        final String tokenName = "some_valid_token";
        return Map.of(
                TokenValue.of(tokenName),
                Token.of(tokenName, STATIC_NOW));
    }

    @Override
    public Token get(final TokenValue tokenValue) {
        final Token token = tokens.get(tokenValue);
        if (token == null) {
            throw new TokenNotFoundException();
        }
        return token;
    }
}
