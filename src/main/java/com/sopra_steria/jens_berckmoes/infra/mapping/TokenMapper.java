package com.sopra_steria.jens_berckmoes.infra.mapping;

import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import com.sopra_steria.jens_berckmoes.domain.Token;

public class TokenMapper {
    public static Token mapToDomain(final TokenEntity entity) {
        return Token.of(entity.getValue(), entity.getExpirationDate());
    }

    public static TokenEntity mapToInfra(final Token token) {
        return TokenEntity.builder()
                .value(token.token())
                .expirationDate(token.expirationDate())
                .build();
    }
}
