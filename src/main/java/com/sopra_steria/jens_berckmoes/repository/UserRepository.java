package com.sopra_steria.jens_berckmoes.repository;

import com.sopra_steria.jens_berckmoes.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.model.User;
import com.sopra_steria.jens_berckmoes.model.Username;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(Username username);
    User get(final Username username) throws UserNotFoundException;
}
