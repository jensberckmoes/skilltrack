package com.sopra_steria.jens_berckmoes.domain.repository;

import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;
import com.sopra_steria.jens_berckmoes.infra.repository.CrudUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static com.sopra_steria.jens_berckmoes.TestConstants.Users.*;
import static com.sopra_steria.jens_berckmoes.infra.mapping.UserMapper.mapToInfra;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class DatabaseUserRepositoryTest {

    final CrudUserRepository crudUserRepository = mock(CrudUserRepository.class);
    final DatabaseUserRepository repository = new DatabaseUserRepository(crudUserRepository);

    @Test
    @DisplayName("DatabaseUserRepository should find user by username")
    void shouldFindByUsername() {
        when(crudUserRepository.findById(VALID_USERNAME)).thenReturn(Optional.ofNullable(mapToInfra(VALID_USER)));

        final User databaseUsername = repository.findByUsername(VALID_USERNAME);

        assertUserFieldsAreEqual(databaseUsername, VALID_USER);
    }

    @Test
    @DisplayName("DatabaseUserRepository should actually hit the database when finding by username")
    void shouldActuallyHitTheDatabase() {
        when(crudUserRepository.findById(SECOND_VALID_USERNAME)).thenReturn(Optional.ofNullable(mapToInfra(
                SECOND_VALID_USER)));

        final User databaseUsername = repository.findByUsername(SECOND_VALID_USERNAME);

        assertUserFieldsAreEqual(databaseUsername, SECOND_VALID_USER);
        verify(crudUserRepository, times(1)).findById(SECOND_VALID_USERNAME);
    }

    @Test
    @DisplayName("DatabaseUserRepository should throw UserNotFoundException when user is not found by username")
    void shouldThrowUserNotFoundWhenNotFound() {
        when(crudUserRepository.findById("-")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> repository.findByUsername("-")).isInstanceOf(UserNotFoundException.class);
        verify(crudUserRepository, times(1)).findById("-");
    }

    @Test
    @DisplayName("DatabaseUserRepository should save user and return the saved user with correct fields")
    void shouldSaveUser() {
        final UserEntity userEntity = mapToInfra(VALID_USER);
        when(crudUserRepository.save(userEntity)).thenReturn(userEntity);

        final User savedUser = repository.save(VALID_USER);

        assertUserFieldsAreEqual(savedUser, VALID_USER);
    }

    @Test
    @DisplayName("DatabaseUserRepository should actually save to the database when saving a user")
    void shouldActuallySaveToTheDatabase() {
        final UserEntity userEntity = mapToInfra(SECOND_VALID_USER);
        when(crudUserRepository.save(userEntity)).thenReturn(userEntity);

        final User savedUser = repository.save(SECOND_VALID_USER);

        assertUserFieldsAreEqual(savedUser, SECOND_VALID_USER);
        verify(crudUserRepository, times(1)).save(userEntity);
    }

    @Test
    @DisplayName("DatabaseUserRepository should delete all users and actually hit the database when deleting all users")
    void shouldDeleteAllUsers() {
        when(crudUserRepository.findById(VALID_USERNAME)).thenReturn(Optional.of(mapToInfra(VALID_USER)));
        when(crudUserRepository.findById(SECOND_VALID_USERNAME)).thenReturn(Optional.of(mapToInfra(SECOND_VALID_USER)));

        assertThat(repository.findByUsername(VALID_USERNAME)).isNotNull();
        assertThat(repository.findByUsername(SECOND_VALID_USERNAME)).isNotNull();

        repository.deleteAll();

        when(crudUserRepository.findById(VALID_USERNAME)).thenReturn(Optional.empty());
        when(crudUserRepository.findById(SECOND_VALID_USERNAME)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> repository.findByUsername(VALID_USERNAME)).isInstanceOf(UserNotFoundException.class);
        assertThatThrownBy(() -> repository.findByUsername(SECOND_VALID_USERNAME)).isInstanceOf(UserNotFoundException.class);

        verify(crudUserRepository, times(1)).deleteAll();
    }

    @Test
    @DisplayName("DatabaseUserRepository should save all users and return the saved users with correct fields")
    void shouldSaveAllUsers() {
        final Set<UserEntity> entities = mapToInfra(TEST_USERS.values());
        when(crudUserRepository.saveAll(entities)).thenReturn(entities);

        final Set<User> savedUsers = repository.saveAll(entities);

        verify(crudUserRepository, times(1)).saveAll(entities);
        assertThat(savedUsers.size()).isEqualTo(3);
        assertThat(savedUsers.containsAll(TEST_USERS.values())).isEqualTo(true);
    }

    private static void assertUserFieldsAreEqual(final User databaseUsername, final User secondValidUser) {
        assertThat(databaseUsername).isNotNull();
        assertThat(databaseUsername.username()).isEqualTo(secondValidUser.username());
        assertThat(databaseUsername.token()).isNotNull();
        assertThat(databaseUsername.token().token()).isEqualTo(secondValidUser.token().token());
        assertThat(databaseUsername.token().expirationDate()).isEqualTo(secondValidUser.token().expirationDate());
    }

}