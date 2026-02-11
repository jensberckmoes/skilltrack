package com.sopra_steria.jens_berckmoes.domain.repository;

import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;
import com.sopra_steria.jens_berckmoes.infra.repository.CrudUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static com.sopra_steria.jens_berckmoes.TestConstants.Usernames.*;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.*;
import static com.sopra_steria.jens_berckmoes.infra.mapping.UserMapper.mapToInfra;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("DatabaseUserRepository")
class DatabaseUserRepositoryTest {

    final CrudUserRepository crudUserRepository = mock(CrudUserRepository.class);
    final DatabaseUserRepository repository = new DatabaseUserRepository(crudUserRepository);

    @Test
    @DisplayName("should find user by username")
    void shouldFindByUsername() {
        when(crudUserRepository.findById(VALID_USERNAME_FOR_TEN_YEARS_RAW_STRING)).thenReturn(Optional.ofNullable(mapToInfra(
                VALID_USER_FOR_TEN_YEAR)));

        final User databaseUsername = repository.findByUsername(VALID_USERNAME_FOR_TEN_YEARS);

        assertUserFieldsAreEqual(databaseUsername, VALID_USER_FOR_TEN_YEAR);
    }

    @Test
    @DisplayName("should actually hit the database when finding by username")
    void shouldActuallyHitTheDatabase() {
        when(crudUserRepository.findById(VALID_USERNAME_FOR_ONE_MORE_DAY_RAW_STRING)).thenReturn(Optional.ofNullable(mapToInfra(
                VALID_USER_FOR_ONE_MORE_DAY)));

        final User databaseUsername = repository.findByUsername(VALID_USERNAME_FOR_ONE_MORE_DAY);

        assertUserFieldsAreEqual(databaseUsername, VALID_USER_FOR_ONE_MORE_DAY);
        verify(crudUserRepository, times(1)).findById(VALID_USERNAME_FOR_ONE_MORE_DAY_RAW_STRING);
    }

    @Test
    @DisplayName("should throw UserNotFoundException when user is not found by username")
    void shouldThrowUserNotFoundWhenNotFound() {
        when(crudUserRepository.findById(NON_EXISTING_USERNAME_RAW_STRING)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> repository.findByUsername(NON_EXISTING_USERNAME)).isInstanceOf(UserNotFoundException.class);
        verify(crudUserRepository, times(1)).findById(NON_EXISTING_USERNAME_RAW_STRING);
    }

    @Test
    @DisplayName("should save user and return the saved user with correct fields")
    void shouldSaveUser() {
        final UserEntity userEntity = mapToInfra(VALID_USER_FOR_TEN_YEAR);
        when(crudUserRepository.save(userEntity)).thenReturn(userEntity);

        final User savedUser = repository.save(VALID_USER_FOR_TEN_YEAR);

        assertUserFieldsAreEqual(savedUser, VALID_USER_FOR_TEN_YEAR);
    }

    @Test
    @DisplayName("should actually save to the database when saving a user")
    void shouldActuallySaveToTheDatabase() {
        final UserEntity userEntity = mapToInfra(VALID_USER_FOR_ONE_MORE_DAY);
        when(crudUserRepository.save(userEntity)).thenReturn(userEntity);

        final User savedUser = repository.save(VALID_USER_FOR_ONE_MORE_DAY);

        assertUserFieldsAreEqual(savedUser, VALID_USER_FOR_ONE_MORE_DAY);
        verify(crudUserRepository, times(1)).save(userEntity);
    }

    @Test
    @DisplayName("should delete all users and actually hit the database when deleting all users")
    void shouldDeleteAllUsers() {
        when(crudUserRepository.findById(VALID_USERNAME_FOR_TEN_YEARS_RAW_STRING)).thenReturn(Optional.of(mapToInfra(
                VALID_USER_FOR_TEN_YEAR)));
        when(crudUserRepository.findById(VALID_USERNAME_FOR_ONE_MORE_DAY_RAW_STRING)).thenReturn(Optional.of(mapToInfra(
                VALID_USER_FOR_ONE_MORE_DAY)));

        assertThat(repository.findByUsername(VALID_USERNAME_FOR_TEN_YEARS)).isNotNull();
        assertThat(repository.findByUsername(VALID_USERNAME_FOR_ONE_MORE_DAY)).isNotNull();

        repository.deleteAll();

        when(crudUserRepository.findById(VALID_USERNAME_FOR_TEN_YEARS_RAW_STRING)).thenReturn(Optional.empty());
        when(crudUserRepository.findById(VALID_USERNAME_FOR_ONE_MORE_DAY_RAW_STRING)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> repository.findByUsername(VALID_USERNAME_FOR_TEN_YEARS)).isInstanceOf(UserNotFoundException.class);
        assertThatThrownBy(() -> repository.findByUsername(VALID_USERNAME_FOR_ONE_MORE_DAY)).isInstanceOf(UserNotFoundException.class);

        verify(crudUserRepository, times(1)).deleteAll();
    }

    @Test
    @DisplayName("should save all users and return the saved users with correct fields")
    void shouldSaveAllUsers() {
        final Set<UserEntity> entities = mapToInfra(USERS_AS_SET);
        when(crudUserRepository.saveAll(entities)).thenReturn(entities);

        final Set<User> savedUsers = repository.saveAll(entities);

        assertThat(savedUsers.size()).isEqualTo(3);
        assertThat(savedUsers.containsAll(USERS_AS_SET)).isTrue();
        verify(crudUserRepository, times(1)).saveAll(entities);
    }

    private static void assertUserFieldsAreEqual(final User databaseUsername, final User secondValidUser) {
        assertThat(databaseUsername).isNotNull();
        assertThat(databaseUsername.username()).isEqualTo(secondValidUser.username());
        assertThat(databaseUsername.token()).isNotNull();
        assertThat(databaseUsername.token().token()).isEqualTo(secondValidUser.token().token());
        assertThat(databaseUsername.token().expirationDate()).isEqualTo(secondValidUser.token().expirationDate());
    }

}