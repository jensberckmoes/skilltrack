package com.sopra_steria.jens_berckmoes.bdd.fakes;

import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenNotFoundException;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenValueNullException;
import com.sopra_steria.jens_berckmoes.domain.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.domain.valueobject.TokenValue;
import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import com.sopra_steria.jens_berckmoes.infra.mapping.TokenMapper;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public record InMemoryTokenRepository(Map<String, Token> tokens) implements TokenRepository {

    @Override
    public Token findByTokenValue(final TokenValue tokenValue) throws TokenNotFoundException {
        if(tokenValue == null) {
            throw new TokenValueNullException("TokenValue cannot be null");
        }
        return Optional.ofNullable(tokens.get(tokenValue.value())).orElseThrow(() -> new TokenNotFoundException("Token not found: " + tokenValue.value()));
    }

    @Override
    public Token save(final Token token) {
        return tokens.put(token.token(), token);
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
