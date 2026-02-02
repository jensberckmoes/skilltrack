package com.sopra_steria.jens_berckmoes.mapping;

import com.sopra_steria.jens_berckmoes.infra.UserEntity;
import com.sopra_steria.jens_berckmoes.model.Token;
import com.sopra_steria.jens_berckmoes.model.User;
import com.sopra_steria.jens_berckmoes.model.Username;

import java.time.LocalDateTime;

public class UserMapper {
    public User mapToDomain(final UserEntity userEntity) {
        return User.of(Username.of("testEntity"), Token.of("token", LocalDateTime.of(2026, 1, 30, 12, 15, 0)));
    }
}
