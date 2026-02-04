package com.sopra_steria.jens_berckmoes.domain.repository;

import com.sopra_steria.jens_berckmoes.domain.exception.TokenNotFoundException;
import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.valueobject.TokenValue;

public interface TokenRepository {
    Token get(final TokenValue token) throws TokenNotFoundException;
}
