package com.sopra_steria.jens_berckmoes.infra;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class TokenEntity {
    private String value;
    private LocalDateTime expirationDate;
}
