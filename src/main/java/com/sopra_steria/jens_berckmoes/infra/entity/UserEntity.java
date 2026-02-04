package com.sopra_steria.jens_berckmoes.infra.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "app_user")
@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @Column(name = "user_name")
    private String username;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "token_token_value", referencedColumnName = "token_value")
    private TokenEntity token;
}
