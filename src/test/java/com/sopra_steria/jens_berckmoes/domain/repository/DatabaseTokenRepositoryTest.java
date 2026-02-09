package com.sopra_steria.jens_berckmoes.domain.repository;

import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenNotFoundException;
import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import com.sopra_steria.jens_berckmoes.infra.repository.CrudTokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.*;
import static com.sopra_steria.jens_berckmoes.infra.mapping.TokenMapper.toInfra;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("DatabaseTokenRepository")
class DatabaseTokenRepositoryTest {

    final CrudTokenRepository crudTokenRepository = mock(CrudTokenRepository.class);
    final TokenRepository repository = new DatabaseTokenRepository(crudTokenRepository);

    @Test
    @DisplayName("should find token by token value")
    void shouldFindTokenByTokenValue() {
        when(crudTokenRepository.findById(VALID_TOKEN_FOR_TEN_YEARS_RAW_STRING)).thenReturn(Optional.ofNullable(toInfra(
                VALID_TOKEN_FOR_TEN_YEARS)));

        final Token tokenOnDatabase = repository.findByTokenValue(VALID_TOKEN_FOR_TEN_YEARS_RAW_STRING);

        assertTokenFieldsAreEqual(tokenOnDatabase, VALID_TOKEN_FOR_TEN_YEARS);
    }

    @Test
    @DisplayName("should actually find a token using the database when finding by token value")
    void shouldActuallyHitTheDatabase() {
        when(crudTokenRepository.findById(VALID_TOKEN_FOR_ONE_MORE_DAY_RAW_STRING)).thenReturn(Optional.ofNullable(
                toInfra(VALID_TOKEN_FOR_ONE_MORE_DAY)));

        final Token tokenOnDatabase = repository.findByTokenValue(VALID_TOKEN_FOR_ONE_MORE_DAY_RAW_STRING);

        assertTokenFieldsAreEqual(tokenOnDatabase, VALID_TOKEN_FOR_ONE_MORE_DAY);
        verify(crudTokenRepository, times(1)).findById(VALID_TOKEN_FOR_ONE_MORE_DAY_RAW_STRING);
    }

    @Test
    @DisplayName("should throw TokenNotFoundException when token is not found by token value")
    void shouldThrowTokenNotFoundWhenNotFound() {
        when(crudTokenRepository.findById("-")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> repository.findByTokenValue("-")).isInstanceOf(TokenNotFoundException.class);
        verify(crudTokenRepository, times(1)).findById("-");
    }

    @Test
    @DisplayName("should save token and return the saved token with correct fields")
    void shouldSaveToken() {
        when(crudTokenRepository.save(toInfra(VALID_TOKEN_FOR_TEN_YEARS))).thenReturn(toInfra(VALID_TOKEN_FOR_TEN_YEARS));

        final Token savedToken = repository.save(VALID_TOKEN_FOR_TEN_YEARS);

        assertTokenFieldsAreEqual(savedToken, VALID_TOKEN_FOR_TEN_YEARS);
    }

    @Test
    @DisplayName("should actually save to the database when saving a token")
    void shouldActuallySaveToTheDatabase() {
        final TokenEntity tokenEntity = toInfra(VALID_TOKEN_FOR_ONE_MORE_DAY);
        when(crudTokenRepository.save(tokenEntity)).thenReturn(tokenEntity);

        final Token savedToken = repository.save(VALID_TOKEN_FOR_ONE_MORE_DAY);

        assertTokenFieldsAreEqual(savedToken, VALID_TOKEN_FOR_ONE_MORE_DAY);
        verify(crudTokenRepository, times(1)).save(tokenEntity);
    }

    @Test
    @DisplayName("should delete all tokens and throw TokenNotFoundException when trying to find deleted tokens")
    void shouldDeleteAllTokens() {
        when(crudTokenRepository.findById(VALID_TOKEN_FOR_TEN_YEARS_RAW_STRING)).thenReturn(Optional.of(toInfra(
                VALID_TOKEN_FOR_TEN_YEARS)));
        when(crudTokenRepository.findById(VALID_TOKEN_FOR_ONE_MORE_DAY_RAW_STRING)).thenReturn(Optional.of(toInfra(
                VALID_TOKEN_FOR_ONE_MORE_DAY)));

        assertThat(repository.findByTokenValue(VALID_TOKEN_FOR_TEN_YEARS_RAW_STRING)).isNotNull();
        assertThat(repository.findByTokenValue(VALID_TOKEN_FOR_ONE_MORE_DAY_RAW_STRING)).isNotNull();

        repository.deleteAll();

        when(crudTokenRepository.findById(VALID_TOKEN_FOR_TEN_YEARS_RAW_STRING)).thenReturn(Optional.empty());
        when(crudTokenRepository.findById(VALID_TOKEN_FOR_ONE_MORE_DAY_RAW_STRING)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> repository.findByTokenValue(VALID_TOKEN_FOR_TEN_YEARS_RAW_STRING)).isInstanceOf(
                TokenNotFoundException.class);
        assertThatThrownBy(() -> repository.findByTokenValue(VALID_TOKEN_FOR_ONE_MORE_DAY_RAW_STRING)).isInstanceOf(
                TokenNotFoundException.class);

        verify(crudTokenRepository, times(1)).deleteAll();
    }

    @Test
    @DisplayName("should save all tokens and return the saved tokens with correct fields")
    void shouldSaveAllTokens() {
        final Set<TokenEntity> entities = toInfra(TOKENS_AS_SET);
        when(crudTokenRepository.saveAll(entities)).thenReturn(entities);

        final Set<Token> savedTokens = repository.saveAll(entities);

        assertThat(savedTokens.size()).isEqualTo(3);
        assertThat(savedTokens.containsAll(TOKENS_AS_SET)).isTrue();
        verify(crudTokenRepository, times(1)).saveAll(entities);
    }

    private static void assertTokenFieldsAreEqual(final Token tokenOnDatabase, final Token validToken) {
        assertThat(tokenOnDatabase).isNotNull();
        assertThat(tokenOnDatabase.expirationDate()).isEqualTo(validToken.expirationDate());
        assertThat(tokenOnDatabase.token()).isEqualTo(validToken.token());
    }

}