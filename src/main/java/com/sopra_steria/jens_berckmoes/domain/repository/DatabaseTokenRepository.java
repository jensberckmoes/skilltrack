package com.sopra_steria.jens_berckmoes.domain.repository;

import com.sopra_steria.jens_berckmoes.domain.exception.TokenNotFoundException;
import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.valueobject.TokenValue;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("prod")
public class DatabaseTokenRepository implements TokenRepository {
    @Override
    public Token get(final TokenValue token) throws TokenNotFoundException {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
