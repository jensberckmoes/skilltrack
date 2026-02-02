package com.sopra_steria.jens_berckmoes.repository;

import com.sopra_steria.jens_berckmoes.exception.TokenNotFoundException;
import com.sopra_steria.jens_berckmoes.model.Token;
import com.sopra_steria.jens_berckmoes.model.TokenValue;
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
