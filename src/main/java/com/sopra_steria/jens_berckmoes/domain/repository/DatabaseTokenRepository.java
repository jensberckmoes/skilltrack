package com.sopra_steria.jens_berckmoes.domain.repository;

import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenNotFoundException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("prod")
public class DatabaseTokenRepository implements TokenRepository {
    @Override
    public Token findByTokenValue(final String token) throws TokenNotFoundException {
        return null;
    }
}
