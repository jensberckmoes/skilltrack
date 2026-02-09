package com.sopra_steria.jens_berckmoes.infra.mapping;

import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.sopra_steria.jens_berckmoes.TestConstants.TokenEntities.VALID_TOKEN_ENTITY_FOR_ONE_MORE_DAY;
import static com.sopra_steria.jens_berckmoes.TestConstants.TokenEntities.VALID_TOKEN_ENTITY_FOR_TEN_YEARS;
import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.VALID_TOKEN_FOR_ONE_MORE_DAY;
import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.VALID_TOKEN_FOR_TEN_YEARS;
import static com.sopra_steria.jens_berckmoes.infra.mapping.TokenMapper.toDomain;
import static com.sopra_steria.jens_berckmoes.infra.mapping.TokenMapper.toInfra;

@DisplayName("TokenMapper")
class TokenMapperTest {
    @Test
    @DisplayName("should correctly map from TokenEntity to Token")
    void shouldMapUserEntityToUserCorrectly() {
        final Token mappedResult = toDomain(VALID_TOKEN_ENTITY_FOR_TEN_YEARS);

        Assertions.assertThat(mappedResult).isEqualTo(VALID_TOKEN_FOR_TEN_YEARS);
    }

    @Test
    @DisplayName("should correctly map from Token to TokenEntity")
    void shouldMapUserToUserEntityCorrectly() {
        final TokenEntity mappedResult = toInfra(VALID_TOKEN_FOR_TEN_YEARS);

        Assertions.assertThat(mappedResult).isEqualTo(VALID_TOKEN_ENTITY_FOR_TEN_YEARS);
    }

    @Test
    @DisplayName("should correctly map from TokenEntitySet to TokenSet")
    void shouldMapUserEntitySetToUserSetCorrectly() {
        final Set<Token> mappedResult = toDomain(Set.of(VALID_TOKEN_ENTITY_FOR_TEN_YEARS,VALID_TOKEN_ENTITY_FOR_ONE_MORE_DAY));

        Assertions.assertThat(mappedResult).isEqualTo(Set.of(VALID_TOKEN_FOR_TEN_YEARS, VALID_TOKEN_FOR_ONE_MORE_DAY));
    }

    @Test
    @DisplayName("should correctly map from TokenSet to TokenEntitySet")
    void shouldMapUserSetToUserEntitySetCorrectly() {
        final Set<TokenEntity> mappedResult = toInfra(Set.of(VALID_TOKEN_FOR_TEN_YEARS, VALID_TOKEN_FOR_ONE_MORE_DAY));

        Assertions.assertThat(mappedResult).isEqualTo(Set.of(VALID_TOKEN_ENTITY_FOR_TEN_YEARS,VALID_TOKEN_ENTITY_FOR_ONE_MORE_DAY));
    }
}
