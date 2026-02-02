package com.sopra_steria.jens_berckmoes.infra;

import lombok.Builder;

@Builder
public class UserEntity {
    private String username;
    private TokenEntity token;
}
