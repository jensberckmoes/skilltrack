package com.sopra_steria.jens_berckmoes.repository;

import com.sopra_steria.jens_berckmoes.exception.TokenNotFoundException;
import com.sopra_steria.jens_berckmoes.model.Token;
import com.sopra_steria.jens_berckmoes.model.TokenValue;

import java.util.Map;

public interface TokenRepository {
    Token get(final TokenValue token) throws TokenNotFoundException;
    Map<TokenValue, Token> getTokens();
}
