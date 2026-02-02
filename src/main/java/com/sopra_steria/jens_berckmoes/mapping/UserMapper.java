package com.sopra_steria.jens_berckmoes.mapping;

import com.sopra_steria.jens_berckmoes.infra.UserEntity;
import com.sopra_steria.jens_berckmoes.model.User;
import com.sopra_steria.jens_berckmoes.model.Username;

public class UserMapper {
    public static User mapToDomain(final UserEntity entity) {
        return User.of(
                Username.of(entity.getUsername()),
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
