package com.sopra_steria.jens_berckmoes.domain.dto;

public record UserDto(String username) {
    public static UserDto of(final String username) {
        return new UserDto(username);
    }
}
