package com.sopra_steria.jens_berckmoes.domain.repository;

import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.infra.mapping.UserMapper;
import com.sopra_steria.jens_berckmoes.infra.repository.CrudUserRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.sopra_steria.jens_berckmoes.TestConstants.Users.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class DatabaseUserRepositoryTest {

    final CrudUserRepository crudUserRepository = mock(CrudUserRepository.class);
    final DatabaseUserRepository repository = new DatabaseUserRepository(crudUserRepository);

    @Test
    void shouldFindByUsername() {
        when(crudUserRepository.findById(VALID_USERNAME)).thenReturn(Optional.ofNullable(UserMapper.mapToInfra(VALID_USER)));

        final User databaseUsername = repository.findByUsername(VALID_USERNAME);

        assertUserFieldsAreEqual(databaseUsername, VALID_USER);
    }

    @Test
    void shouldActuallyHitTheDatabase() {
        when(crudUserRepository.findById(SECOND_VALID_USERNAME)).thenReturn(Optional.ofNullable(UserMapper.mapToInfra(SECOND_VALID_USER)));

        final User databaseUsername = repository.findByUsername(SECOND_VALID_USERNAME);

        assertUserFieldsAreEqual(databaseUsername, SECOND_VALID_USER);
        verify(crudUserRepository, times(1)).findById(SECOND_VALID_USERNAME);
    }

    @Test
    void shouldThrowUserNotFoundWhenNotFound() {
        when(crudUserRepository.findById("-")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> repository.findByUsername("-")).isInstanceOf(UserNotFoundException.class);
        verify(crudUserRepository, times(1)).findById("-");
    }

    private static void assertUserFieldsAreEqual(final User databaseUsername, final User secondValidUser) {
        assertThat(databaseUsername).isNotNull();
        assertThat(databaseUsername.username()).isEqualTo(secondValidUser.username());
        assertThat(databaseUsername.token()).isNotNull();
        assertThat(databaseUsername.token().token()).isEqualTo(secondValidUser.token().token());
        assertThat(databaseUsername.token().expirationDate()).isEqualTo(secondValidUser.token().expirationDate());
    }

}