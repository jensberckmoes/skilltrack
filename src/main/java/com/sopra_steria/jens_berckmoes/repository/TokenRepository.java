package com.sopra_steria.jens_berckmoes.repository;

import com.sopra_steria.jens_berckmoes.model.Token;

import java.time.LocalDateTime;

public class TokenRepository {
    public boolean exists(final String token) {
        return "valid_token".equals(token);
    }

    public Token get(String token) {
       return Token.of("valid_token", LocalDateTime.now().plusDays(5));
    }
}
