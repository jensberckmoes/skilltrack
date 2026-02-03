package com.sopra_steria.jens_berckmoes.infra.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "token")
public class TokenEntity {
    @Id
    @Column(name = "token_value")
    private String value;
    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;
}
