package com.sopra_steria.jens_berckmoes.domain.repository;

import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.infra.mapping.UserMapper;
import com.sopra_steria.jens_berckmoes.infra.repository.CrudUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("prod")
@AllArgsConstructor
public class DatabaseUserRepository implements UserRepository {
    private final CrudUserRepository crudUserRepository;

    @Override
    public User findByUsername(final String username) throws UserNotFoundException {
        return crudUserRepository.findById(username)
                .map(UserMapper::mapToDomain)
                .orElseThrow(UserNotFoundException::new);

    }
}
