package com.sopra_steria.jens_berckmoes.infra.mapping;

import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.sopra_steria.jens_berckmoes.TestConstants.UserEntities.USER_ENTITY;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.VALID_USER;
import static com.sopra_steria.jens_berckmoes.infra.mapping.UserMapper.mapToDomain;
import static com.sopra_steria.jens_berckmoes.infra.mapping.UserMapper.mapToInfra;


@DisplayName("UserMapper")
class UserMapperTest {

    @Test
    @DisplayName("should correctly map from UserEntity to User")
    void shouldMapUserEntityToUserCorrectly() {
        final User mappedResult = mapToDomain(USER_ENTITY);

        Assertions.assertThat(mappedResult).isEqualTo(VALID_USER);
    }

    @Test
    @DisplayName("should correctly map from User to UserEntity")
    void shouldMapUserToUserEntityCorrectly() {
        final UserEntity mappedResult = mapToInfra(VALID_USER);

        Assertions.assertThat(mappedResult).isEqualTo(USER_ENTITY);
    }
}
