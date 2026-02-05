package com.sopra_steria.jens_berckmoes.domain.repository;

import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenNotFoundException;
import com.sopra_steria.jens_berckmoes.infra.mapping.TokenMapper;
import com.sopra_steria.jens_berckmoes.infra.repository.CrudTokenRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class DatabaseTokenRepositoryTest {

    final CrudTokenRepository crudTokenRepository = mock(CrudTokenRepository.class);
    final TokenRepository repository = new DatabaseTokenRepository(crudTokenRepository);

    @Test
    void shouldFindTokenByTokenValue() {
        when(crudTokenRepository.findById(VALID_RAW_TOKEN)).thenReturn(Optional.ofNullable(TokenMapper.mapToInfra(VALID_TOKEN)));

        final Token tokenOnDatabase = repository.findByTokenValue(VALID_RAW_TOKEN);

        assertTokenFieldsAreEqual(tokenOnDatabase, VALID_TOKEN);
    }

    @Test
    void shouldActuallyHitTheDatabase() {
        when(crudTokenRepository.findById(SECOND_VALID_RAW_TOKEN)).thenReturn(Optional.ofNullable(TokenMapper.mapToInfra(SECOND_VALID_TOKEN)));

        final Token tokenOnDatabase = repository.findByTokenValue(SECOND_VALID_RAW_TOKEN);

        assertTokenFieldsAreEqual(tokenOnDatabase, SECOND_VALID_TOKEN);
        verify(crudTokenRepository, times(1)).findById(SECOND_VALID_RAW_TOKEN);
    }

    @Test
    void shouldThrowTokenNotFoundWhenNotFound() {
        when(crudTokenRepository.findById("-")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> repository.findByTokenValue("-")).isInstanceOf(TokenNotFoundException.class);
        verify(crudTokenRepository, times(1)).findById("-");
    }


    private static void assertTokenFieldsAreEqual(final Token tokenOnDatabase, final Token validToken) {
        assertThat(tokenOnDatabase).isNotNull();
        assertThat(tokenOnDatabase.expirationDate()).isEqualTo(validToken.expirationDate());
        assertThat(tokenOnDatabase.token()).isEqualTo(validToken.token());
    }

}