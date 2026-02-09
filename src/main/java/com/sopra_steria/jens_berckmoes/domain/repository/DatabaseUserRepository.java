package com.sopra_steria.jens_berckmoes.domain.repository;

import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;
import com.sopra_steria.jens_berckmoes.infra.mapping.UserMapper;
import com.sopra_steria.jens_berckmoes.infra.repository.CrudUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

import static com.sopra_steria.jens_berckmoes.infra.mapping.UserMapper.mapToDomain;
import static com.sopra_steria.jens_berckmoes.infra.mapping.UserMapper.mapToInfra;


@Repository
@AllArgsConstructor
public class DatabaseUserRepository implements UserRepository {
    private final CrudUserRepository crudUserRepository;

    @Override
    public User findByUsername(final String username) throws UserNotFoundException {
        return crudUserRepository.findById(username)
                .map(UserMapper::mapToDomain)
                .orElseThrow(UserNotFoundException::new);

    }

    @Override
    public Set<User> findALl() {
        return Set.of();
    }

    @Override
    public User save(final User user) {
        final UserEntity entityToSave = mapToInfra(user);
        return mapToDomain(crudUserRepository.save(entityToSave));
    }

    @Override
    public void deleteAll() {
        crudUserRepository.deleteAll();
    }

    @Override
    public Set<User> saveAll(final Collection<UserEntity> entities) {
        return mapToDomain(crudUserRepository.saveAll(entities));
    }
}
