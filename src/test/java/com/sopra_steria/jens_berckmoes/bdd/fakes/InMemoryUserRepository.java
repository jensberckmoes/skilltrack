package com.sopra_steria.jens_berckmoes.bdd.fakes;

import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.domain.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
@Profile("test")
public record InMemoryUserRepository(Map<String, User> users) implements UserRepository {

    @Override
    public User findByUsername(final String username) throws UserNotFoundException {
        return Optional.ofNullable(users.get(username))
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User save(final User of) {
        return null;
    }
}
