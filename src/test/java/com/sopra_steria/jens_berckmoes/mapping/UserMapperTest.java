package com.sopra_steria.jens_berckmoes.mapping;

import com.sopra_steria.jens_berckmoes.TestConstants;
import com.sopra_steria.jens_berckmoes.infra.TokenEntity;
import com.sopra_steria.jens_berckmoes.infra.UserEntity;
import com.sopra_steria.jens_berckmoes.model.Token;
import com.sopra_steria.jens_berckmoes.model.User;
import com.sopra_steria.jens_berckmoes.model.Username;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class UserMapperTest {

    @Test
    void shouldMapUserEntityToUserCorrectly() {
        final TokenEntity tokenEntityToMap = TokenEntity.builder()
                .value("hashed-token")
                .expirationDate(TestConstants.Tokens.STATIC_NOW)
                .build();
        final UserEntity userEntityToMap = UserEntity.builder()
                .username("testEntity")
                .token(tokenEntityToMap)
                .build();
        final User assertedUser = User.of(Username.of("testEntity"), Token.of("hashed-token", TestConstants.Tokens.STATIC_NOW));

        final User mappedResult = UserMapper.mapToDomain(userEntityToMap);

        Assertions.assertThat(mappedResult).isEqualTo(assertedUser);
    }

    @Test
    void shouldMapUserToUserEntityCorrectly() {
        final User userToMap = User.of(Username.of("testEntity"), Token.of("hashed-token", TestConstants.Tokens.STATIC_NOW));
        final TokenEntity assertedTokenEntity = TokenEntity.builder()
                .value("hashed-token")
                .expirationDate(TestConstants.Tokens.STATIC_NOW)
                .build();
        final UserEntity assertedUserEntity = UserEntity.builder()
                .username("testEntity")
                .token(assertedTokenEntity)
                .build();

        final UserEntity mappedResult = UserMapper.mapToInfra(userToMap);

        Assertions.assertThat(mappedResult).isEqualTo(assertedUserEntity);
    }
}
