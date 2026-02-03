package com.sopra_steria.jens_berckmoes.infra.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserEntity {
    private String username;
    private TokenEntity token;
}
