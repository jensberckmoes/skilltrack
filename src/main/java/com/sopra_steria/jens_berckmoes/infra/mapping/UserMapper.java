package com.sopra_steria.jens_berckmoes.infra.mapping;

import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;
import com.sopra_steria.jens_berckmoes.util.StreamUtils;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {
    public static User mapToDomain(final UserEntity entity) {
        return User.of(entity.getUsername(), TokenMapper.mapToDomain(entity.getToken()));
    }

    public static UserEntity mapToInfra(final User user) {
        return UserEntity.builder().username(user.username()).token(TokenMapper.mapToInfra(user.token())).build();
    }

    public static Set<User> mapToDomain(final Iterable<UserEntity> entities) {
        return StreamUtils.toStream(entities).map(UserMapper::mapToDomain).collect(Collectors.toSet());
    }

    public static Set<UserEntity> mapToInfra(final Iterable<User> entities) {
        return StreamUtils.toStream(entities).map(UserMapper::mapToInfra).collect(Collectors.toSet());
    }
}
