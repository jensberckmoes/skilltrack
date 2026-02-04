package com.sopra_steria.jens_berckmoes.infra.repository;

import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import com.sopra_steria.jens_berckmoes.infra.mapping.TokenMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.VALID_TOKEN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("CrudTokenRepository Integration Tests")
class CrudTokenRepositoryTest {

    @Autowired
    private CrudTokenRepository tokenRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldSaveAndRetrieveToken() {
        final TokenEntity token = TokenMapper.mapToInfra(VALID_TOKEN);
        tokenRepository.save(token);
        flushAndResetContext();

        final TokenEntity retrieved = tokenRepository.findById(token.getValue()).orElseThrow();
        assertThat(retrieved).isEqualTo(token);
    }

    @Test
    void shouldEnforceNotNullExpirationDate() {
        assertThatThrownBy(() -> {
            tokenRepository.save(new TokenEntity("token-1", null));
            entityManager.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldDeleteToken() {
        final TokenEntity token = new TokenEntity("token-1", LocalDate.now());
        tokenRepository.save(token);

        tokenRepository.delete(token);
        assertThat(tokenRepository.findById("token-1")).isEmpty();
    }

    @Test
    void shouldUpdateToken() {
        final TokenEntity token = TokenMapper.mapToInfra(VALID_TOKEN);
        tokenRepository.save(token);
        flushAndResetContext();

        final TokenEntity retrieved = tokenRepository.findById(token.getValue()).orElseThrow();
        final int amountOfDaysToUpdate = 14;
        retrieved.setExpirationDate(retrieved.getExpirationDate().plusDays(amountOfDaysToUpdate));
        tokenRepository.save(retrieved);
        flushAndResetContext();

        final TokenEntity updated = tokenRepository.findById(token.getValue()).orElseThrow();
        assertThat(updated.getExpirationDate()).isEqualTo(token.getExpirationDate().plusDays(amountOfDaysToUpdate));
    }

    private void flushAndResetContext() {
        entityManager.flush();
        entityManager.clear();
    }

}
