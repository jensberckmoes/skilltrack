package com.sopra_steria.jens_berckmoes.infra.mapping;

import com.sopra_steria.jens_berckmoes.TestConstants;
import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.valueobject.Username;
import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class UserMapperTest {
    private final LocalDate referenceDate = TestConstants.TimeFixture.REFERENCE_DATE;

    @Test
    void shouldMapUserEntityToUserCorrectly() {
        final TokenEntity tokenEntityToMap = TokenEntity.builder()
                .value("hashed-token")
                .expirationDate(referenceDate)
                .build();
        final UserEntity userEntityToMap = UserEntity.builder()
                .username("testEntity")
                .token(tokenEntityToMap)
                .build();
        final User assertedUser = User.of(Username.of("testEntity"), Token.of("hashed-token", referenceDate));

        final User mappedResult = UserMapper.mapToDomain(userEntityToMap);

        Assertions.assertThat(mappedResult).isEqualTo(assertedUser);
    }

    @Test
    void shouldMapUserToUserEntityCorrectly() {
        final User userToMap = User.of(Username.of("testEntity"), Token.of("hashed-token", referenceDate));
        final TokenEntity assertedTokenEntity = TokenEntity.builder()
                .value("hashed-token")
                .expirationDate(referenceDate)
                .build();
        final UserEntity assertedUserEntity = UserEntity.builder()
                .username("testEntity")
                .token(assertedTokenEntity)
                .build();

        final UserEntity mappedResult = UserMapper.mapToInfra(userToMap);

        Assertions.assertThat(mappedResult).isEqualTo(assertedUserEntity);
    }
}
