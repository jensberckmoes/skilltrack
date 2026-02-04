package com.sopra_steria.jens_berckmoes.infra.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TokenEntity {
    @Id
    @Column(name = "token_value")
    private String value;
    private LocalDateTime expirationDate;
}
