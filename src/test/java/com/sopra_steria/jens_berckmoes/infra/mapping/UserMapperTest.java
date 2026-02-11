package com.sopra_steria.jens_berckmoes.infra.mapping;

import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.sopra_steria.jens_berckmoes.TestConstants.UserEntities.ALICE_ENTITY;
import static com.sopra_steria.jens_berckmoes.TestConstants.UserEntities.BOB_ENTITY;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.ALICE;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.BOB;
import static com.sopra_steria.jens_berckmoes.infra.mapping.UserMapper.mapToDomain;
import static com.sopra_steria.jens_berckmoes.infra.mapping.UserMapper.mapToInfra;


@DisplayName("UserMapper")
class UserMapperTest {

    @Test
    @DisplayName("should correctly map from UserEntity to User")
    void shouldMapUserEntityToUserCorrectly() {
        final User mappedResult = mapToDomain(ALICE_ENTITY);

        Assertions.assertThat(mappedResult).isEqualTo(ALICE);
    }

    @Test
    @DisplayName("should correctly map from User to UserEntity")
    void shouldMapUserToUserEntityCorrectly() {
        final UserEntity mappedResult = mapToInfra(ALICE);

        Assertions.assertThat(mappedResult).isEqualTo(ALICE_ENTITY);
    }

    @Test
    @DisplayName("should correctly map from UserEntitySet to UserSet")
    void shouldMapUserEntitySetToUserSetCorrectly() {
        final Set<User> mappedResult = mapToDomain(Set.of(ALICE_ENTITY, BOB_ENTITY));

        Assertions.assertThat(mappedResult).isEqualTo(Set.of(ALICE, BOB));
    }

    @Test
    @DisplayName("should correctly map from UserSet to UserEntitySet")
    void shouldMapUserSetToUserEntitySetCorrectly() {
        final Set<UserEntity> mappedResult = mapToInfra(Set.of(ALICE, BOB));

        Assertions.assertThat(mappedResult).isEqualTo(Set.of(ALICE_ENTITY, BOB_ENTITY));
    }
}
