package com.sopra_steria.jens_berckmoes.domain.repository;

import com.sopra_steria.jens_berckmoes.domain.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.valueobject.Username;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(Username username);
    User get(final Username username) throws UserNotFoundException;
}
