package com.sopra_steria.jens_berckmoes.domain.repository;

import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;

import java.util.Collection;
import java.util.Set;

public interface UserRepository {
    User findByUsername(final String username) throws UserNotFoundException;

    Set<User> findAll();

    User save(final User of);

    void deleteAll();

    Set<User> saveAll(final Collection<UserEntity> entities);

}
