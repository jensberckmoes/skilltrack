package com.sopra_steria.jens_berckmoes.infra.mapping;

import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;
import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.valueobject.Username;

public class UserMapper {
    public static User mapToDomain(final UserEntity entity) {
        return User.of(
                entity.getUsername(),
                TokenMapper.mapToDomain(entity.getToken())
        );
    }

    public static UserEntity mapToInfra(final User user) {
        return UserEntity.builder()
                .username(user.username())
                .token(TokenMapper.mapToInfra(user.token()))
                .build();
    }
}
