package com.sopra_steria.jens_berckmoes.bdd.fakes;

import com.sopra_steria.jens_berckmoes.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.model.Token;
import com.sopra_steria.jens_berckmoes.model.User;
import com.sopra_steria.jens_berckmoes.model.Username;
import com.sopra_steria.jens_berckmoes.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Map;

public class InMemoryUserRepository implements UserRepository {
    private static final LocalDateTime STATIC_NOW = LocalDateTime.of(2026, 1, 30, 12, 15, 0);
    private final Map<Username, User> users = construct();

    private Map<Username, User> construct() {
        final Username username = Username.of("jane.doe@example.com");
        return Map.of(username, User.of(username, Token.of("some_valid_token", STATIC_NOW)));
    }

    @Override
    public User get(final Username username) throws UserNotFoundException {
        final User user = users.get(username);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

}
