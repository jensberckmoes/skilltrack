package com.sopra_steria.jens_berckmoes.domain.repository;

import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenNotFoundException;
import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;

import java.util.Collection;
import java.util.Set;

public interface TokenRepository {
    Token findByTokenValue(final String token) throws TokenNotFoundException;

    Token save(final Token token);

    void deleteAll();

    Set<Token> saveAll(final Collection<TokenEntity> entities);
}
