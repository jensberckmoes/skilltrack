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
import static com.sopra_steria.jens_berckmoes.infra.mapping.TokenMapper.mapToInfra;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class DatabaseTokenRepositoryTest {

    final CrudTokenRepository crudTokenRepository = mock(CrudTokenRepository.class);
    final TokenRepository repository = new DatabaseTokenRepository(crudTokenRepository);

    @Test
    @DisplayName("DatabaseTokenRepository should find token by token value")
    void shouldFindTokenByTokenValue() {
        when(crudTokenRepository.findById(VALID_RAW_TOKEN)).thenReturn(Optional.ofNullable(mapToInfra(VALID_TOKEN)));

        final Token tokenOnDatabase = repository.findByTokenValue(VALID_RAW_TOKEN);

        assertTokenFieldsAreEqual(tokenOnDatabase, VALID_TOKEN);
    }

    @Test
    @DisplayName("DatabaseTokenRepository should actually hit the database when finding by token value")
    void shouldActuallyHitTheDatabase() {
        when(crudTokenRepository.findById(SECOND_VALID_RAW_TOKEN)).thenReturn(Optional.ofNullable(mapToInfra(
                SECOND_VALID_TOKEN)));

        final Token tokenOnDatabase = repository.findByTokenValue(SECOND_VALID_RAW_TOKEN);

        assertTokenFieldsAreEqual(tokenOnDatabase, SECOND_VALID_TOKEN);
        verify(crudTokenRepository, times(1)).findById(SECOND_VALID_RAW_TOKEN);
    }

    @Test
    @DisplayName("DatabaseTokenRepository should throw TokenNotFoundException when token is not found by token value")
    void shouldThrowTokenNotFoundWhenNotFound() {
        when(crudTokenRepository.findById("-")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> repository.findByTokenValue("-")).isInstanceOf(TokenNotFoundException.class);
        verify(crudTokenRepository, times(1)).findById("-");
    }

    @Test
    @DisplayName("DatabaseTokenRepository should save token and return the saved token with correct fields")
    void shouldSaveToken() {
        when(crudTokenRepository.save(mapToInfra(VALID_TOKEN))).thenReturn(mapToInfra(VALID_TOKEN));

        final Token savedToken = repository.save(VALID_TOKEN);

        assertTokenFieldsAreEqual(savedToken, VALID_TOKEN);
    }

    @Test
    @DisplayName("DatabaseTokenRepository should actually save to the database when saving a token")
    void shouldActuallySaveToTheDatabase() {
        final TokenEntity tokenEntity = mapToInfra(SECOND_VALID_TOKEN);
        when(crudTokenRepository.save(tokenEntity)).thenReturn(tokenEntity);

        final Token savedToken = repository.save(SECOND_VALID_TOKEN);

        assertTokenFieldsAreEqual(savedToken, SECOND_VALID_TOKEN);
        verify(crudTokenRepository, times(1)).save(tokenEntity);
    }

    @Test
    @DisplayName("DatabaseTokenRepository should delete all tokens and throw TokenNotFoundException when trying to find deleted tokens")
    void shouldDeleteAllUsers() {
        when(crudTokenRepository.findById(VALID_RAW_TOKEN)).thenReturn(Optional.of(mapToInfra(VALID_TOKEN)));
        when(crudTokenRepository.findById(SECOND_VALID_RAW_TOKEN)).thenReturn(Optional.of(mapToInfra(SECOND_VALID_TOKEN)));

        assertThat(repository.findByTokenValue(VALID_RAW_TOKEN)).isNotNull();
        assertThat(repository.findByTokenValue(SECOND_VALID_RAW_TOKEN)).isNotNull();

        repository.deleteAll();

        when(crudTokenRepository.findById(VALID_RAW_TOKEN)).thenReturn(Optional.empty());
        when(crudTokenRepository.findById(SECOND_VALID_RAW_TOKEN)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> repository.findByTokenValue(VALID_RAW_TOKEN)).isInstanceOf(TokenNotFoundException.class);
        assertThatThrownBy(() -> repository.findByTokenValue(SECOND_VALID_RAW_TOKEN)).isInstanceOf(
                TokenNotFoundException.class);

        verify(crudTokenRepository, times(1)).deleteAll();
    }

    @Test
    @DisplayName("DatabaseTokenRepository should save all tokens and return the saved tokens with correct fields")
    void shouldSaveAllTokens() {
        final Set<TokenEntity> entities = mapToInfra(TEST_TOKENS.values());
        when(crudTokenRepository.saveAll(entities)).thenReturn(entities);

        final Set<Token> savedTokens = repository.saveAll(entities);

        verify(crudTokenRepository, times(1)).saveAll(entities);
        assertThat(savedTokens.size()).isEqualTo(3);
        assertThat(savedTokens.containsAll(TEST_TOKENS.values())).isEqualTo(true);
    }

    private static void assertTokenFieldsAreEqual(final Token tokenOnDatabase, final Token validToken) {
        assertThat(tokenOnDatabase).isNotNull();
        assertThat(tokenOnDatabase.expirationDate()).isEqualTo(validToken.expirationDate());
        assertThat(tokenOnDatabase.token()).isEqualTo(validToken.token());
    }

}