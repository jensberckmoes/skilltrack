package com.sopra_steria.jens_berckmoes.infra.mapping;

import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import com.sopra_steria.jens_berckmoes.util.StreamUtils;

import java.util.Set;
import java.util.stream.Collectors;

public class TokenMapper {
    public static Token toDomain(final TokenEntity entity) {
        return Token.of(entity.getValue(), entity.getExpirationDate());
    }

    public static TokenEntity toInfra(final Token token) {
        return TokenEntity.builder().value(token.token()).expirationDate(token.expirationDate()).build();
    }

    public static Set<Token> toDomain(final Iterable<TokenEntity> entities) {
        return StreamUtils.toStream(entities).map(TokenMapper::toDomain).collect(Collectors.toSet());
    }

    public static Set<TokenEntity> toInfra(final Iterable<Token> entities) {
        return StreamUtils.toStream(entities).map(TokenMapper::toInfra).collect(Collectors.toSet());
    }
}
