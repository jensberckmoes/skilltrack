package com.sopra_steria.jens_berckmoes.bdd.fakes;

import com.sopra_steria.jens_berckmoes.exception.TokenNotFoundException;
import com.sopra_steria.jens_berckmoes.model.Token;
import com.sopra_steria.jens_berckmoes.model.TokenValue;
import com.sopra_steria.jens_berckmoes.repository.TokenRepository;

import java.util.Map;

public record InMemoryTokenRepository(Map<TokenValue, Token> tokens) implements TokenRepository {

    @Override
    public Token get(final TokenValue tokenValue) {
        final Token token = tokens.get(tokenValue);
        if (token == null) {
            throw new TokenNotFoundException();
        }
        return token;
    }
}
