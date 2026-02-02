package com.sopra_steria.jens_berckmoes.bdd.fakes;

import com.sopra_steria.jens_berckmoes.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.model.User;
import com.sopra_steria.jens_berckmoes.model.Username;
import com.sopra_steria.jens_berckmoes.repository.UserRepository;

import java.util.Map;
import java.util.Optional;


public record InMemoryUserRepository(Map<Username, User> users) implements UserRepository {

    @Override
    public Optional<User> findByUsername(Username username) {
        return Optional.ofNullable(users.get(username));
    }

    @Override
    public User get(final Username username) throws UserNotFoundException {
        return findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

}
