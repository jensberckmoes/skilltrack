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
    void shouldMapUserToUserEntityCorrectly() {
        final TokenEntity tokenEntity = TokenEntity.builder()
                .value("hashed-token")
                .expirationDate(TestConstants.Tokens.STATIC_NOW)
                .build();
        final UserEntity userEntity = UserEntity.builder()
                .username("testEntity")
                .token(tokenEntity)
                .build();
        final UserMapper userMapper = new UserMapper();
        final User user = userMapper.mapToDomain(userEntity);
        final User userAssert = User.of(Username.of("testEntity"), Token.of("token", TestConstants.Tokens.STATIC_NOW));
        Assertions.assertThat(user).isEqualTo(userAssert);
    }
}
