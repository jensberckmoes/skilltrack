package com.sopra_steria.jens_berckmoes.domain.repository;

import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenNotFoundException;
import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import com.sopra_steria.jens_berckmoes.infra.mapping.TokenMapper;
import com.sopra_steria.jens_berckmoes.infra.repository.CrudTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("prod")
@AllArgsConstructor
public class DatabaseTokenRepository implements TokenRepository {
    private final CrudTokenRepository crudTokenRepository;

    @Override
    public Token findByTokenValue(final String token) throws TokenNotFoundException {
        final TokenEntity possibleTokenEntity = crudTokenRepository.findById(token)
                .orElseThrow(TokenNotFoundException::new);
        return TokenMapper.mapToDomain(possibleTokenEntity);
    }
}
