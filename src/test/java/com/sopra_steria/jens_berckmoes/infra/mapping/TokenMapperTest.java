package com.sopra_steria.jens_berckmoes.infra.mapping;

import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.sopra_steria.jens_berckmoes.TestConstants.TokenEntities.VALID_TOKEN_ENTITY_FOR_TEN_YEARS;
import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.VALID_TOKEN_FOR_TEN_YEARS;

@DisplayName("TokenMapper")
class TokenMapperTest {
    @Test
    @DisplayName("should correctly map from TokenEntity to Token")
    void shouldMapUserEntityToUserCorrectly() {
        final Token mappedResult = TokenMapper.mapToDomain(VALID_TOKEN_ENTITY_FOR_TEN_YEARS);

        Assertions.assertThat(mappedResult).isEqualTo(VALID_TOKEN_FOR_TEN_YEARS);
    }

    @Test
    @DisplayName("should correctly map from Token to TokenEntity")
    void shouldMapUserToUserEntityCorrectly() {
        final TokenEntity mappedResult = TokenMapper.mapToInfra(VALID_TOKEN_FOR_TEN_YEARS);

        Assertions.assertThat(mappedResult).isEqualTo(VALID_TOKEN_ENTITY_FOR_TEN_YEARS);
    }
}
