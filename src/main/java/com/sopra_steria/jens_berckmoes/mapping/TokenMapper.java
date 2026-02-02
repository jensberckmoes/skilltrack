package com.sopra_steria.jens_berckmoes.mapping;

import com.sopra_steria.jens_berckmoes.infra.TokenEntity;
import com.sopra_steria.jens_berckmoes.model.Token;

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
