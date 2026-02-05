package com.sopra_steria.jens_berckmoes.domain.repository;

import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.domain.valueobject.Username;
import com.sopra_steria.jens_berckmoes.infra.repository.CrudUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
@Profile("prod")
@AllArgsConstructor
public class DatabaseUserRepository implements UserRepository {
    private final CrudUserRepository crudUserRepository;

    @Override
    public User findByUsername(final String username) throws UserNotFoundException {
        return User.of(Username.of("jane.doe@example.com"), Token.of("valid_token", LocalDate.of(2026, 1, 30).plusYears(10)));
    }
}
