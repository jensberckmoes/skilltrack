package com.sopra_steria.jens_berckmoes.domain.dto;

public record CreateUserRequest(String username) {
    public static CreateUserRequest of(final String username) {
        return new CreateUserRequest(username);
    }
}
