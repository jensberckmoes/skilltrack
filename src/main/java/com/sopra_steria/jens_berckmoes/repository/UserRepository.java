package com.sopra_steria.jens_berckmoes.repository;

import com.sopra_steria.jens_berckmoes.model.User;

public class UserRepository {
    public boolean exists(final String username) {
        return "jane.doe@example.com".equals(username);
    }

    public User get(String token) {
        return User.of("jane.doe@example.com", "some_valid_token");
    }
}
