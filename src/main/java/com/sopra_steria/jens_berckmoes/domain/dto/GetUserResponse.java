package com.sopra_steria.jens_berckmoes.domain.dto;

public record GetUserResponse(String username) {
    public static GetUserResponse of(final String username) {
        return new GetUserResponse(username);
    }
}
