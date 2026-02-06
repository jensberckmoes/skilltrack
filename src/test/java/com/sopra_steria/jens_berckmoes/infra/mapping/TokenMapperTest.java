package com.sopra_steria.jens_berckmoes.infra.mapping;

import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.sopra_steria.jens_berckmoes.TestConstants.TokenEntities.TOKEN_ENTITY;
import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.VALID_TOKEN;

@DisplayName("TokenMapper")
class TokenMapperTest {
    @Test
    @DisplayName("should correctly map from TokenEntity to Token")
    void shouldMapUserEntityToUserCorrectly() {
        final Token mappedResult = TokenMapper.mapToDomain(TOKEN_ENTITY);

        Assertions.assertThat(mappedResult).isEqualTo(VALID_TOKEN);
    }

    @Test
    @DisplayName("should correctly map from Token to TokenEntity")
    void shouldMapUserToUserEntityCorrectly() {
        final TokenEntity mappedResult = TokenMapper.mapToInfra(VALID_TOKEN);

        Assertions.assertThat(mappedResult).isEqualTo(TOKEN_ENTITY);
    }
}
