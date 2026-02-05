package com.sopra_steria.jens_berckmoes.domain.repository;

import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.infra.mapping.UserMapper;
import com.sopra_steria.jens_berckmoes.infra.repository.CrudUserRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.sopra_steria.jens_berckmoes.TestConstants.Users.VALID_USER;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.VALID_USERNAME;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DatabaseUserRepositoryTest {

    @Test
    void shouldFindByUsername() {
        final CrudUserRepository crudUserRepository = mock(CrudUserRepository.class);
        final DatabaseUserRepository repository = new DatabaseUserRepository(crudUserRepository);

        when(crudUserRepository.findById(VALID_USERNAME)).thenReturn(Optional.ofNullable(UserMapper.mapToInfra(VALID_USER)));

        final User databaseUsername = repository.findByUsername(VALID_USERNAME);

        assertThat(databaseUsername).isNotNull();
        assertThat(databaseUsername.username()).isEqualTo(VALID_USER.username());
        assertThat(databaseUsername.token()).isNotNull();
        assertThat(databaseUsername.token().token()).isEqualTo(VALID_USER.token().token());
        assertThat(databaseUsername.token().expirationDate()).isEqualTo(VALID_USER.token().expirationDate());
    }

}