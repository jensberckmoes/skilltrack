package com.sopra_steria.jens_berckmoes.bdd.fakes;

import com.sopra_steria.jens_berckmoes.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.model.User;
import com.sopra_steria.jens_berckmoes.model.Username;
import com.sopra_steria.jens_berckmoes.repository.UserRepository;

import java.util.Map;


public record InMemoryUserRepository(Map<Username, User> users) implements UserRepository {

    @Override
    public User get(final Username username) throws UserNotFoundException {
        final User user = users.get(username);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

}
