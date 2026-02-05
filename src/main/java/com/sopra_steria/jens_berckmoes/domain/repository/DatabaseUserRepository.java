package com.sopra_steria.jens_berckmoes.domain.repository;

import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.exception.UserNotFoundException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("prod")
public class DatabaseUserRepository implements UserRepository {
    @Override
    public User findByUsername(final String username) throws UserNotFoundException {
        return null;
    }
}
