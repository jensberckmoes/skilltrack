package com.sopra_steria.jens_berckmoes.domain.repository;

import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.domain.exception.UsernameNullException;
import com.sopra_steria.jens_berckmoes.domain.valueobject.Username;
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
    public User findByUsername(final Username username) throws UserNotFoundException {
        if(username == null) {
            throw new UsernameNullException("Username cannot be null");
        }
        return crudUserRepository.findById(username.value())
                .map(UserMapper::mapToDomain)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username.value()));

    }

    @Override
    public Set<User> findAll() {
        return mapToDomain(crudUserRepository.findAll());
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
