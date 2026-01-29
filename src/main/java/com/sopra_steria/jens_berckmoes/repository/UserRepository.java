package com.sopra_steria.jens_berckmoes.repository;

import com.sopra_steria.jens_berckmoes.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.model.User;
import com.sopra_steria.jens_berckmoes.model.Username;

public class UserRepository {

    public User get(final Username username) throws UserNotFoundException {
        try {
            return User.defaultUser(username);
        } catch (final IllegalArgumentException e) {
            throw new UserNotFoundException();
        }
    }
}
