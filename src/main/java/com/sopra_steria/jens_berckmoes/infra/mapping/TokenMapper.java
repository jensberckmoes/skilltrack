package com.sopra_steria.jens_berckmoes.infra.mapping;

import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import com.sopra_steria.jens_berckmoes.util.StreamUtils;

import java.util.Set;
import java.util.stream.Collectors;

public class TokenMapper {
    public static Token mapToDomain(final TokenEntity entity) {
        return Token.of(entity.getValue(), entity.getExpirationDate());
    }

    public static TokenEntity mapToInfra(final Token token) {
        return TokenEntity.builder().value(token.token()).expirationDate(token.expirationDate()).build();
    }

    public static Set<Token> mapToDomain(final Iterable<TokenEntity> entities) {
        return StreamUtils.toStream(entities).map(TokenMapper::mapToDomain).collect(Collectors.toSet());
    }

    public static Set<TokenEntity> mapToInfra(final Iterable<Token> entities) {
        return StreamUtils.toStream(entities).map(TokenMapper::mapToInfra).collect(Collectors.toSet());
    }
}
