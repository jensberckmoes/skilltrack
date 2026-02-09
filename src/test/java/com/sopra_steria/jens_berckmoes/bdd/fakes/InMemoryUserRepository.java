package com.sopra_steria.jens_berckmoes.bdd.fakes;

import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.domain.repository.UserRepository;
import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;
import com.sopra_steria.jens_berckmoes.infra.mapping.UserMapper;

import java.util.*;
import java.util.stream.Collectors;

public record InMemoryUserRepository(Map<String, User> users) implements UserRepository {

    @Override
    public User findByUsername(final String username) throws UserNotFoundException {
        return Optional.ofNullable(users.get(username)).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Set<User> findAll() {
        return new HashSet<>(users.values());
    }

    @Override
    public User save(final User of) {
        users.put(of.username(), of);
        return of;
    }

    @Override
    public void deleteAll() {
        users.clear();
    }

    @Override
    public Set<User> saveAll(final Collection<UserEntity> entities) {
        return entities.stream()
                .map(UserMapper::mapToDomain)
                .peek(user -> users.put(user.username(), user))
                .collect(Collectors.toSet());
    }
}
