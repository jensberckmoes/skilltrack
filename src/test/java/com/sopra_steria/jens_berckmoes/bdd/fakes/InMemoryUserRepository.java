package com.sopra_steria.jens_berckmoes.bdd.fakes;

import com.sopra_steria.jens_berckmoes.domain.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.valueobject.Username;
import com.sopra_steria.jens_berckmoes.domain.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
@Profile("test")
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
