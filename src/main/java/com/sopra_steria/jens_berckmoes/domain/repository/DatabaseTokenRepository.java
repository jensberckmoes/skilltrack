package com.sopra_steria.jens_berckmoes.domain.repository;

import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenNotFoundException;
import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import com.sopra_steria.jens_berckmoes.infra.mapping.TokenMapper;
import com.sopra_steria.jens_berckmoes.infra.repository.CrudTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import static com.sopra_steria.jens_berckmoes.infra.mapping.TokenMapper.mapToDomain;
import static com.sopra_steria.jens_berckmoes.infra.mapping.TokenMapper.mapToInfra;

@Repository
@Profile("prod")
@AllArgsConstructor
public class DatabaseTokenRepository implements TokenRepository {
    private final CrudTokenRepository crudTokenRepository;

    @Override
    public Token findByTokenValue(final String token) throws TokenNotFoundException {
        return crudTokenRepository.findById(token)
                .map(TokenMapper::mapToDomain)
                .orElseThrow(TokenNotFoundException::new);
    }

    @Override
    public Token save(final Token token) {
        final TokenEntity entityToSave = mapToInfra(token);
        return mapToDomain(crudTokenRepository.save(entityToSave));
    }
}
