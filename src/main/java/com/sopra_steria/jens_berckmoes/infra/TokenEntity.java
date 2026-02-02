package com.sopra_steria.jens_berckmoes.infra;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class TokenEntity {
    private String value;
    private LocalDateTime expirationDate;
}
