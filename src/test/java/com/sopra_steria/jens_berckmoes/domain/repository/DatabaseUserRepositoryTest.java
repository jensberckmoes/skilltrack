package com.sopra_steria.jens_berckmoes.domain.repository;

import com.sopra_steria.jens_berckmoes.bdd.fakes.StepResult;
import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.domain.exception.UsernameNullException;
import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;
import com.sopra_steria.jens_berckmoes.infra.repository.CrudUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static com.sopra_steria.jens_berckmoes.TestConstants.UserEntities.*;
import static com.sopra_steria.jens_berckmoes.TestConstants.Usernames.ALICE_USERNAME;
import static com.sopra_steria.jens_berckmoes.TestConstants.Usernames.NON_EXISTING_USERNAME;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.*;
import static com.sopra_steria.jens_berckmoes.bdd.fakes.StepResult.callController;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("DatabaseUserRepository")
class DatabaseUserRepositoryTest {

    final CrudUserRepository crudUserRepository = mock(CrudUserRepository.class);
    final DatabaseUserRepository domainRepository = new DatabaseUserRepository(crudUserRepository);

    @Test
    @DisplayName("should throw UsernameNullException when token value is null")
    void shouldThrowWhenTokenValueIsNull() {
        final StepResult<User> result = callController(() -> domainRepository.findByUsername(null));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.exception()).isInstanceOf(UsernameNullException.class);
        assertThat(result.exception().getMessage()).isEqualTo("Username cannot be null");
        verify(crudUserRepository, times(0)).findById(any());
    }

    @Test
    @DisplayName("should actually find a user using the database when findByUsername")
    void shouldActuallyHitTheDatabase() {
        when(crudUserRepository.findById(ALICE.username())).thenReturn(Optional.ofNullable(ALICE_ENTITY));

        final StepResult<User> result = callController(() -> domainRepository.findByUsername(ALICE_USERNAME));

        assertThat(result.isSuccess()).isTrue();
        assertUserFieldsAreEqual(result.body(), ALICE);
        verify(crudUserRepository, times(1)).findById(ALICE.username());
    }

    @Test
    @DisplayName("should throw UserNotFoundException when user is not found by username")
    void shouldThrowUserNotFoundWhenNotFound() {
        when(crudUserRepository.findById(NON_EXISTING_USERNAME_RAW_STRING)).thenReturn(Optional.empty());

        final StepResult<User> result = callController(() -> domainRepository.findByUsername(NON_EXISTING_USERNAME));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.exception()).isInstanceOf(UserNotFoundException.class);
        assertThat(result.exception().getMessage()).isEqualTo("User not found: " + NON_EXISTING_USERNAME_RAW_STRING);
        verify(crudUserRepository, times(1)).findById(NON_EXISTING_USERNAME_RAW_STRING);
    }

    @Test
    @DisplayName("should actually save to the database when saving a user")
    void shouldActuallySaveToTheDatabase() {
        when(crudUserRepository.save(BOB_ENTITY)).thenReturn(BOB_ENTITY);

        final StepResult<User> result = callController(() -> domainRepository.save(BOB));

        assertThat(result.isSuccess()).isTrue();
        assertUserFieldsAreEqual(result.body(), BOB);
        verify(crudUserRepository, times(1)).save(BOB_ENTITY);
    }

    @Test
    @DisplayName("should return empty set when saving an empty set of tokens")
    void shouldReturnEmptySetWhenSavingEmptySet() {
        when(crudUserRepository.saveAll(any())).thenReturn(Set.of());

        final StepResult<Set<User>> result = callController(() -> domainRepository.saveAll(Set.of()));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.body()).isEqualTo(Set.of());
        verify(crudUserRepository, times(1)).saveAll(any());
    }

    @Test
    @DisplayName("should be able to delete all users")
    void shouldDeleteAllTokens2() {
        domainRepository.deleteAll();

        verify(crudUserRepository, times(1)).deleteAll();
    }

    @Test
    @DisplayName("should save all users and return the saved users with correct fields")
    void shouldSaveAllUsers() {
        final Set<UserEntity> entities = Set.of(ALICE_ENTITY, BOB_ENTITY, CHARLIE_ENTITY);
        when(crudUserRepository.saveAll(entities)).thenReturn(entities);

        final StepResult<Set<User>> result = callController(() -> domainRepository.saveAll(entities));

        assertThat(result.isSuccess()).isTrue();
        final Set<User> savedUsers = result.body();
        assertThat(savedUsers.size()).isEqualTo(3);
        assertThat(savedUsers.containsAll(Set.of(ALICE, BOB, CHARLIE))).isTrue();
        verify(crudUserRepository, times(1)).saveAll(entities);

    }

    @Test
    @DisplayName("should have an idempotent deleteAll method")
    void deleteAllIsIdempotent() {
        domainRepository.deleteAll();
        domainRepository.deleteAll();
        verify(crudUserRepository, times(2)).deleteAll();
    }


    private static void assertUserFieldsAreEqual(final User databaseUsername, final User secondValidUser) {
        assertThat(databaseUsername).isNotNull();
        assertThat(databaseUsername.username()).isEqualTo(secondValidUser.username());
        assertThat(databaseUsername.token()).isNotNull();
        assertThat(databaseUsername.token().token()).isEqualTo(secondValidUser.token().token());
        assertThat(databaseUsername.token().expirationDate()).isEqualTo(secondValidUser.token().expirationDate());
    }

}