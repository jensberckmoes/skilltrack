package com.sopra_steria.jens_berckmoes.domain.repository;

import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenNotFoundException;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenValueNullException;
import com.sopra_steria.jens_berckmoes.domain.valueobject.TokenValue;
import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import com.sopra_steria.jens_berckmoes.infra.mapping.TokenMapper;
import com.sopra_steria.jens_berckmoes.infra.repository.CrudTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

import static com.sopra_steria.jens_berckmoes.infra.mapping.TokenMapper.toDomain;
import static com.sopra_steria.jens_berckmoes.infra.mapping.TokenMapper.toInfra;

@Repository
@AllArgsConstructor
public class DatabaseTokenRepository implements TokenRepository {
    private final CrudTokenRepository crudTokenRepository;

    @Override
    public Token findByTokenValue(final TokenValue token) throws TokenNotFoundException {
        if (token == null) {
            throw new TokenValueNullException("TokenValue cannot be null");
        }
        return crudTokenRepository.findById(token.value())
                .map(TokenMapper::toDomain)
                .orElseThrow(() -> new TokenNotFoundException("Token not found: " + token.value()));
    }

    @Override
    public Token save(final Token token) {
        final TokenEntity entityToSave = toInfra(token);
        return toDomain(crudTokenRepository.save(entityToSave));
    }

    @Override
    public void deleteAll() {
        crudTokenRepository.deleteAll();
    }

    @Override
    public Set<Token> saveAll(final Collection<TokenEntity> entities) {
        return toDomain(crudTokenRepository.saveAll(entities));
    }
}
