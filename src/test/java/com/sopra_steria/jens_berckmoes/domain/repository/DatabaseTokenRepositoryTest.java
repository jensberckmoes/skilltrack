package com.sopra_steria.jens_berckmoes.domain.repository;

import com.sopra_steria.jens_berckmoes.bdd.fakes.StepResult;
import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenNotFoundException;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenValueNullException;
import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import com.sopra_steria.jens_berckmoes.infra.repository.CrudTokenRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static com.sopra_steria.jens_berckmoes.TestConstants.TokenEntities.*;
import static com.sopra_steria.jens_berckmoes.TestConstants.TokenValues.*;
import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.*;
import static com.sopra_steria.jens_berckmoes.bdd.fakes.StepResult.callController;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("DatabaseTokenRepository")
class DatabaseTokenRepositoryTest {

    final CrudTokenRepository crudTokenRepository = mock(CrudTokenRepository.class);
    final TokenRepository repository = new DatabaseTokenRepository(crudTokenRepository);

    @Test
    @DisplayName("should throw NullPointerException when token value is null")
    void shouldThrowWhenTokenValueIsNull() {
        final StepResult<Token> result = callController(() -> repository.findByTokenValue(null));

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.exceptionMessage()).isEqualTo("TokenValue cannot be null");
    }

    @Test
    @DisplayName("should actually find a token using the database when finding by token value")
    void shouldActuallyHitTheDatabase() {
        when(crudTokenRepository.findById(BOB_TOKEN.token())).thenReturn(Optional.ofNullable(BOB_TOKEN_ENTITY));

        final StepResult<Token> result = callController(() -> repository.findByTokenValue(BOB_TOKEN_VALUE));

        assertTokenFieldsAreEqual(result.body(), BOB_TOKEN);
        assertThat(result.isSuccess()).isTrue();
        verify(crudTokenRepository, times(1)).findById(BOB_TOKEN.token());
    }

    @Test
    @DisplayName("should throw TokenNotFoundException when token is not found by token value")
    void shouldThrowTokenNotFoundWhenNotFound() {
        when(crudTokenRepository.findById(ALICE_TOKEN.token())).thenReturn(Optional.empty());

        final StepResult<Token> result = callController(() -> repository.findByTokenValue(ALICE_TOKEN_VALUE));

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.exceptionMessage()).isEqualTo("Token not found: " + ALICE_TOKEN.token());
        verify(crudTokenRepository, times(1)).findById(ALICE_TOKEN.token());
    }

    @Test
    @DisplayName("should save token and return the saved token with correct fields")
    void shouldSaveToken() {
        when(crudTokenRepository.save(ALICE_TOKEN_ENTITY)).thenReturn(ALICE_TOKEN_ENTITY);

        final Token savedToken = repository.save(ALICE_TOKEN);

        assertTokenFieldsAreEqual(savedToken, ALICE_TOKEN);
    }

    @Test
    @DisplayName("should actually save to the database when saving a token")
    void shouldActuallySaveToTheDatabase() {
        when(crudTokenRepository.save(ALICE_TOKEN_ENTITY)).thenReturn(ALICE_TOKEN_ENTITY);

        final Token savedToken = repository.save(ALICE_TOKEN);

        assertTokenFieldsAreEqual(savedToken, ALICE_TOKEN);
        verify(crudTokenRepository, times(1)).save(ALICE_TOKEN_ENTITY);
    }

    @Test
    @DisplayName("should return empty set when saving an empty set of tokens")
    void shouldReturnEmptySetWhenSavingEmptySet() {
        final Set<Token> saved = repository.saveAll(Set.of());
        Assertions.assertThat(saved).isEmpty();
    }

    @Test
    @DisplayName("should delete all tokens and throw TokenNotFoundException when trying to find deleted tokens")
    void shouldDeleteAllTokens() {
        when(crudTokenRepository.findById(ALICE_TOKEN.token())).thenReturn(Optional.of(ALICE_TOKEN_ENTITY));
        when(crudTokenRepository.findById(BOB_TOKEN.token())).thenReturn(Optional.of(BOB_TOKEN_ENTITY));

        assertThat(repository.findByTokenValue(ALICE_TOKEN_VALUE)).isNotNull();
        assertThat(repository.findByTokenValue(BOB_TOKEN_VALUE)).isNotNull();

        repository.deleteAll();

        when(crudTokenRepository.findById(ALICE_TOKEN.token())).thenReturn(Optional.empty());
        when(crudTokenRepository.findById(BOB_TOKEN.token())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> repository.findByTokenValue(ALICE_TOKEN_VALUE)).isInstanceOf(
                TokenNotFoundException.class);
        assertThatThrownBy(() -> repository.findByTokenValue(BOB_TOKEN_VALUE)).isInstanceOf(
                TokenNotFoundException.class);

        verify(crudTokenRepository, times(1)).deleteAll();
    }

    @Test
    @DisplayName("should save all tokens and return the saved tokens with correct fields")
    void shouldSaveAllTokens() {
        final Set<TokenEntity> entities = Set.of(ALICE_TOKEN_ENTITY, BOB_TOKEN_ENTITY, CHARLIE_TOKEN_ENTITY);
        when(crudTokenRepository.saveAll(entities)).thenReturn(entities);

        final Set<Token> savedTokens = repository.saveAll(entities);

        assertThat(savedTokens.size()).isEqualTo(3);
        assertThat(savedTokens.containsAll(Set.of(ALICE_TOKEN, BOB_TOKEN, CHARLIE_TOKEN))).isTrue();
        verify(crudTokenRepository, times(1)).saveAll(entities);
    }

    @Test
    @DisplayName("should have an idempotent deleteAll method")
    void deleteAllIsIdempotent() {
        repository.deleteAll();
        repository.deleteAll();
    }

    private static void assertTokenFieldsAreEqual(final Token tokenOnDatabase, final Token validToken) {
        assertThat(tokenOnDatabase).isNotNull();
        assertThat(tokenOnDatabase.expirationDate()).isEqualTo(validToken.expirationDate());
        assertThat(tokenOnDatabase.token()).isEqualTo(validToken.token());
    }

}