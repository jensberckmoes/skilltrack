package com.sopra_steria.jens_berckmoes.infra.mapping;

import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.sopra_steria.jens_berckmoes.TestConstants.TokenEntities.ALICE_TOKEN_ENTITY;
import static com.sopra_steria.jens_berckmoes.TestConstants.TokenEntities.BOB_TOKEN_ENTITY;
import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.ALICE_TOKEN;
import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.BOB_TOKEN;
import static com.sopra_steria.jens_berckmoes.infra.mapping.TokenMapper.toDomain;
import static com.sopra_steria.jens_berckmoes.infra.mapping.TokenMapper.toInfra;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TokenMapper")
class TokenMapperTest {
    @Test
    @DisplayName("should correctly map from TokenEntity to Token")
    void shouldMapUserEntityToUserCorrectly() {
        final Token mappedResult = toDomain(ALICE_TOKEN_ENTITY);

        assertThat(mappedResult).isEqualTo(ALICE_TOKEN);
    }

    @Test
    @DisplayName("should correctly map from Token to TokenEntity")
    void shouldMapUserToUserEntityCorrectly() {
        final TokenEntity mappedResult = toInfra(ALICE_TOKEN);

        assertThat(mappedResult).isEqualTo(ALICE_TOKEN_ENTITY);
    }

    @Test
    @DisplayName("should correctly map from TokenEntitySet to TokenSet")
    void shouldMapUserEntitySetToUserSetCorrectly() {
        final Set<Token> mappedResult = toDomain(Set.of(ALICE_TOKEN_ENTITY, BOB_TOKEN_ENTITY));

        assertThat(mappedResult).isEqualTo(Set.of(ALICE_TOKEN, BOB_TOKEN));
    }

    @Test
    @DisplayName("should correctly map from TokenSet to TokenEntitySet")
    void shouldMapUserSetToUserEntitySetCorrectly() {
        final Set<TokenEntity> mappedResult = toInfra(Set.of(ALICE_TOKEN, BOB_TOKEN));

        assertThat(mappedResult).isEqualTo(Set.of(ALICE_TOKEN_ENTITY, BOB_TOKEN_ENTITY));
    }
}
