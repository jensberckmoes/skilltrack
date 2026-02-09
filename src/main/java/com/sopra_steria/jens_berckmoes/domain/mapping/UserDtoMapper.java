package com.sopra_steria.jens_berckmoes.domain.mapping;

import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.dto.UserDto;

import java.util.Set;

public class UserDtoMapper {

    public static UserDto toDto(final User username) {
        return UserDto.of(username.username());
    }

    public static User toUser(final UserDto dto) {
        return User.nullToken(dto.username());
    }

    public static Set<UserDto> toDto(final Set<User> users) {
        return users.stream().map(UserDtoMapper::toDto).collect(java.util.stream.Collectors.toSet());
    }

    public static Set<User> toUsers(final Set<UserDto> dtos) {
        return dtos.stream().map(UserDtoMapper::toUser).collect(java.util.stream.Collectors.toSet());
    }
}
