package com.sopra_steria.jens_berckmoes.domain.repository;

import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.exception.UserNotFoundException;

public interface UserRepository {
    User findByUsername(final String username) throws UserNotFoundException;

    User save(final User of);
}
