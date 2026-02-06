package com.sopra_steria.jens_berckmoes.infra.mapping;

import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.sopra_steria.jens_berckmoes.TestConstants.UserEntities.VALID_USER_ENTITY_FOR_TEN_YEARS;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.VALID_USER_FOR_TEN_YEAR;
import static com.sopra_steria.jens_berckmoes.infra.mapping.UserMapper.mapToDomain;
import static com.sopra_steria.jens_berckmoes.infra.mapping.UserMapper.mapToInfra;


@DisplayName("UserMapper")
class UserMapperTest {

    @Test
    @DisplayName("should correctly map from UserEntity to User")
    void shouldMapUserEntityToUserCorrectly() {
        final User mappedResult = mapToDomain(VALID_USER_ENTITY_FOR_TEN_YEARS);

        Assertions.assertThat(mappedResult).isEqualTo(VALID_USER_FOR_TEN_YEAR);
    }

    @Test
    @DisplayName("should correctly map from User to UserEntity")
    void shouldMapUserToUserEntityCorrectly() {
        final UserEntity mappedResult = mapToInfra(VALID_USER_FOR_TEN_YEAR);

        Assertions.assertThat(mappedResult).isEqualTo(VALID_USER_ENTITY_FOR_TEN_YEARS);
    }
}
