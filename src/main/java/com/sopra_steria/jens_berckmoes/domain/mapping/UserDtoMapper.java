package com.sopra_steria.jens_berckmoes.domain.mapping;

import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.dto.GetUserResponse;

import java.util.Collection;
import java.util.Set;

public class UserDtoMapper {

    public static GetUserResponse toGetUserResponse(final User username) {
        return GetUserResponse.of(username.username());
    }

    public static User toUser(final GetUserResponse dto) {
        return User.nullToken(dto.username());
    }

    public static Set<GetUserResponse> toGetUsersResponse(final Collection<User> users) {
        return users.stream().map(UserDtoMapper::toGetUserResponse).collect(java.util.stream.Collectors.toSet());
    }

    public static Set<User> toUsers(final Collection<GetUserResponse> getUserResponses) {
        return getUserResponses.stream().map(UserDtoMapper::toUser).collect(java.util.stream.Collectors.toSet());
    }
}
