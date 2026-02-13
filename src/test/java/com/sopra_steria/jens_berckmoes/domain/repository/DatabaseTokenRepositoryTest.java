package com.sopra_steria.jens_berckmoes.domain.repository;

import com.sopra_steria.jens_berckmoes.bdd.fakes.StepResult;
import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenNotFoundException;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenValueNullException;
import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import com.sopra_steria.jens_berckmoes.infra.repository.CrudTokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static com.sopra_steria.jens_berckmoes.TestConstants.TokenEntities.*;
import static com.sopra_steria.jens_berckmoes.TestConstants.TokenValues.ALICE_TOKEN_VALUE;
import static com.sopra_steria.jens_berckmoes.TestConstants.TokenValues.BOB_TOKEN_VALUE;
import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.*;
import static com.sopra_steria.jens_berckmoes.bdd.fakes.StepResult.callController;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("DatabaseTokenRepository")
class DatabaseTokenRepositoryTest {

    final CrudTokenRepository crudTokenRepository = mock(CrudTokenRepository.class);
    final TokenRepository domainRepository = new DatabaseTokenRepository(crudTokenRepository);

    @Test
    @DisplayName("should throw TokenValueNullException when token value is null")
    void shouldThrowWhenTokenValueIsNull() {
        final StepResult<Token> result = callController(() -> domainRepository.findByTokenValue(null));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.exception()).isInstanceOf(TokenValueNullException.class);
        assertThat(result.exception().getMessage()).isEqualTo("TokenValue cannot be null");
        verify(crudTokenRepository, times(0)).findById(any());
    }

    @Test
    @DisplayName("should actually find a token using the database when findByTokenValue")
    void shouldActuallyHitTheDatabase() {
        when(crudTokenRepository.findById(BOB_TOKEN.token())).thenReturn(Optional.ofNullable(BOB_TOKEN_ENTITY));

        final StepResult<Token> result = callController(() -> domainRepository.findByTokenValue(BOB_TOKEN_VALUE));

        assertThat(result.isSuccess()).isTrue();
        assertTokenFieldsAreEqual(result.body(), BOB_TOKEN);
        verify(crudTokenRepository, times(1)).findById(BOB_TOKEN.token());
    }

    @Test
    @DisplayName("should throw TokenNotFoundException when token is not found by token value")
    void shouldThrowTokenNotFoundWhenNotFound() {
        when(crudTokenRepository.findById(ALICE_TOKEN.token())).thenReturn(Optional.empty());

        final StepResult<Token> result = callController(() -> domainRepository.findByTokenValue(ALICE_TOKEN_VALUE));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.exception()).isInstanceOf(TokenNotFoundException.class);
        assertThat(result.exception().getMessage()).isEqualTo("Token not found: " + ALICE_TOKEN.token());
        verify(crudTokenRepository, times(1)).findById(ALICE_TOKEN.token());
    }

    @Test
    @DisplayName("should actually save to the database when saving a token")
    void shouldActuallySaveToTheDatabase() {
        when(crudTokenRepository.save(ALICE_TOKEN_ENTITY)).thenReturn(ALICE_TOKEN_ENTITY);

        final StepResult<Token> result = callController(() -> domainRepository.save(ALICE_TOKEN));

        assertThat(result.isSuccess()).isTrue();
        assertTokenFieldsAreEqual(result.body(), ALICE_TOKEN);
        verify(crudTokenRepository, times(1)).save(ALICE_TOKEN_ENTITY);
    }

    @Test
    @DisplayName("should return empty set when saving an empty set of tokens")
    void shouldReturnEmptySetWhenSavingEmptySet() {
        when(crudTokenRepository.saveAll(any())).thenReturn(Set.of());

        final StepResult<Set<Token>> result = callController(()-> domainRepository.saveAll(Set.of()));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.body()).isEqualTo(Set.of());
        verify(crudTokenRepository,times(1)).saveAll(any());
    }

    @Test
    @DisplayName("should be able to delete all tokens")
    void shouldDeleteAllTokens2() {
        domainRepository.deleteAll();

        verify(crudTokenRepository, times(1)).deleteAll();
    }


    @Test
    @DisplayName("should save all tokens and return the saved tokens with correct fields")
    void shouldSaveAllTokens() {
        final Set<TokenEntity> entities = Set.of(ALICE_TOKEN_ENTITY, BOB_TOKEN_ENTITY, CHARLIE_TOKEN_ENTITY);
        when(crudTokenRepository.saveAll(entities)).thenReturn(entities);

        final StepResult<Set<Token>> result = callController(()-> domainRepository.saveAll(entities));

        assertThat(result.isSuccess()).isTrue();
        final Set<Token> savedTokens = result.body();
        assertThat(savedTokens.size()).isEqualTo(3);
        assertThat(savedTokens.containsAll(Set.of(ALICE_TOKEN, BOB_TOKEN, CHARLIE_TOKEN))).isTrue();
        verify(crudTokenRepository, times(1)).saveAll(entities);
    }

    @Test
    @DisplayName("should have an idempotent deleteAll method")
    void deleteAllIsIdempotent() {
        domainRepository.deleteAll();
        domainRepository.deleteAll();
        verify(crudTokenRepository,times(2)).deleteAll();
    }

    private static void assertTokenFieldsAreEqual(final Token tokenOnDatabase, final Token validToken) {
        assertThat(tokenOnDatabase).isNotNull();
        assertThat(tokenOnDatabase.expirationDate()).isEqualTo(validToken.expirationDate());
        assertThat(tokenOnDatabase.token()).isEqualTo(validToken.token());
    }

}