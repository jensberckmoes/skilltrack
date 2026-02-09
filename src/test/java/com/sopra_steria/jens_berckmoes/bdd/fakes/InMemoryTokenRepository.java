package com.sopra_steria.jens_berckmoes.bdd.fakes;

import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenNotFoundException;
import com.sopra_steria.jens_berckmoes.domain.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import com.sopra_steria.jens_berckmoes.infra.mapping.TokenMapper;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public record InMemoryTokenRepository(Map<String, Token> tokens) implements TokenRepository {

    @Override
    public Token findByTokenValue(final String tokenValue) throws TokenNotFoundException {
        return Optional.ofNullable(tokens.get(tokenValue)).orElseThrow(TokenNotFoundException::new);
    }

    @Override
    public Token save(final Token token) {
        return null;
    }

    @Override
    public void deleteAll() {
        tokens.clear();
    }

    @Override
    public Set<Token> saveAll(final Collection<TokenEntity> entities) {
        return entities.stream()
                .map(TokenMapper::toDomain)
                .peek(token -> tokens.put(token.token(), token))
                .collect(Collectors.toSet());
    }
}
