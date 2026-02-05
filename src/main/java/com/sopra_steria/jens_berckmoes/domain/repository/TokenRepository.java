package com.sopra_steria.jens_berckmoes.domain.repository;

import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenNotFoundException;

public interface TokenRepository {
    Token findByTokenValue(final String token) throws TokenNotFoundException;
}
