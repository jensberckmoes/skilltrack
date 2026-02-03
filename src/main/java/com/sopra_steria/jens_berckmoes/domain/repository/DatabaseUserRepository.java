package com.sopra_steria.jens_berckmoes.domain.repository;

import com.sopra_steria.jens_berckmoes.domain.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.valueobject.Username;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile("prod")
public class DatabaseUserRepository implements UserRepository {
    public Optional<User> findByUsername(final Username username) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public User get(final Username username) throws UserNotFoundException {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
