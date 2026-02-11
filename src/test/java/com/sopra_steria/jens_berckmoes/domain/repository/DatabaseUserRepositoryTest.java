package com.sopra_steria.jens_berckmoes.domain.repository;

import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;
import com.sopra_steria.jens_berckmoes.infra.repository.CrudUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static com.sopra_steria.jens_berckmoes.TestConstants.UserEntities.*;
import static com.sopra_steria.jens_berckmoes.TestConstants.Usernames.*;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.*;
import static com.sopra_steria.jens_berckmoes.infra.mapping.UserMapper.mapToInfra;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("DatabaseUserRepository")
class DatabaseUserRepositoryTest {

    final CrudUserRepository crudUserRepository = mock(CrudUserRepository.class);
    final DatabaseUserRepository domainRepository = new DatabaseUserRepository(crudUserRepository);

    @Test
    @DisplayName("should find user by username")
    void shouldFindByUsername() {
        when(crudUserRepository.findById(ALICE.username())).thenReturn(Optional.ofNullable(ALICE_ENTITY));

        final User databaseUsername = domainRepository.findByUsername(ALICE_USERNAME);

        assertUserFieldsAreEqual(databaseUsername, ALICE);
    }

    @Test
    @DisplayName("should actually hit the database when finding by username")
    void shouldActuallyHitTheDatabase() {
        when(crudUserRepository.findById(BOB.username())).thenReturn(Optional.ofNullable(BOB_ENTITY));

        final User databaseUsername = domainRepository.findByUsername(BOB_USERNAME);

        assertUserFieldsAreEqual(databaseUsername, BOB);
        verify(crudUserRepository, times(1)).findById(BOB.username());
    }

    @Test
    @DisplayName("should throw UserNotFoundException when user is not found by username")
    void shouldThrowUserNotFoundWhenNotFound() {
        when(crudUserRepository.findById(NON_EXISTING_USERNAME_RAW_STRING)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> domainRepository.findByUsername(NON_EXISTING_USERNAME)).isInstanceOf(UserNotFoundException.class);
        verify(crudUserRepository, times(1)).findById(NON_EXISTING_USERNAME_RAW_STRING);
    }

    @Test
    @DisplayName("should save user and return the saved user with correct fields")
    void shouldSaveUser() {
        when(crudUserRepository.save(ALICE_ENTITY)).thenReturn(ALICE_ENTITY);

        final User savedUser = domainRepository.save(ALICE);

        assertUserFieldsAreEqual(savedUser, ALICE);
    }

    @Test
    @DisplayName("should actually save to the database when saving a user")
    void shouldActuallySaveToTheDatabase() {
        when(crudUserRepository.save(BOB_ENTITY)).thenReturn(BOB_ENTITY);

        final User savedUser = domainRepository.save(BOB);

        assertUserFieldsAreEqual(savedUser, BOB);
        verify(crudUserRepository, times(1)).save(BOB_ENTITY);
    }

    @Test
    @DisplayName("should delete all users and actually hit the database when deleting all users")
    void shouldDeleteAllUsers() {
        when(crudUserRepository.findById(ALICE_USERNAME.value())).thenReturn(Optional.of(ALICE_ENTITY));
        when(crudUserRepository.findById(BOB_USERNAME.value())).thenReturn(Optional.of(BOB_ENTITY));

        assertThat(domainRepository.findByUsername(ALICE_USERNAME)).isNotNull();
        assertThat(domainRepository.findByUsername(BOB_USERNAME)).isNotNull();
        verify(crudUserRepository, times(1)).findById(ALICE_USERNAME.value());
        verify(crudUserRepository, times(1)).findById(BOB_USERNAME.value());

        domainRepository.deleteAll();

        when(crudUserRepository.findById(ALICE_USERNAME.value())).thenReturn(Optional.empty());
        when(crudUserRepository.findById(BOB_USERNAME.value())).thenReturn(Optional.empty());
        verify(crudUserRepository, times(1)).findById(ALICE_USERNAME.value());
        verify(crudUserRepository, times(1)).findById(BOB_USERNAME.value());

        assertThatThrownBy(() -> domainRepository.findByUsername(ALICE_USERNAME)).isInstanceOf(UserNotFoundException.class);
        assertThatThrownBy(() -> domainRepository.findByUsername(BOB_USERNAME)).isInstanceOf(UserNotFoundException.class);

        verify(crudUserRepository, times(1)).deleteAll();
    }

    @Test
    @DisplayName("should save all users and return the saved users with correct fields")
    void shouldSaveAllUsers() {
        when(crudUserRepository.saveAll(USER_ENTITIES_AS_SET)).thenReturn(USER_ENTITIES_AS_SET);

        final Set<User> savedUsers = domainRepository.saveAll(USER_ENTITIES_AS_SET);

        assertThat(savedUsers.size()).isEqualTo(USER_ENTITIES_AS_SET.size());
        assertThat(savedUsers.containsAll(USERS_AS_SET)).isTrue();
        verify(crudUserRepository, times(1)).saveAll(USER_ENTITIES_AS_SET);
    }

    private static void assertUserFieldsAreEqual(final User databaseUsername, final User secondValidUser) {
        assertThat(databaseUsername).isNotNull();
        assertThat(databaseUsername.username()).isEqualTo(secondValidUser.username());
        assertThat(databaseUsername.token()).isNotNull();
        assertThat(databaseUsername.token().token()).isEqualTo(secondValidUser.token().token());
        assertThat(databaseUsername.token().expirationDate()).isEqualTo(secondValidUser.token().expirationDate());
    }

}