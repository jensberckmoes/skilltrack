package com.sopra_steria.jens_berckmoes.infra.repository;

import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import com.sopra_steria.jens_berckmoes.infra.mapping.TokenMapper;
import com.sopra_steria.jens_berckmoes.util.StreamUtils;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.sopra_steria.jens_berckmoes.TestConstants.TimeFixture.REFERENCE_DATE;
import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.VALID_TOKEN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("CrudTokenRepository Integration Tests")
class CrudTokenRepositoryTest {

    @Autowired private CrudTokenRepository tokenRepository;

    @Autowired private EntityManager entityManager;

    @Test
    @DisplayName("CrudTokenRepository should save and retrieve token correctly")
    void shouldSaveAndRetrieveToken() {
        final TokenEntity token = TokenMapper.mapToInfra(VALID_TOKEN);
        tokenRepository.save(token);
        flushAndResetContext();

        final TokenEntity retrieved = tokenRepository.findById(token.getValue()).orElseThrow();
        assertThat(retrieved).isEqualTo(token);
    }

    @Test
    @DisplayName("CrudTokenRepository should enforce not null expiration date")
    void shouldEnforceNotNullExpirationDate() {
        assertThatThrownBy(() -> {
            tokenRepository.save(new TokenEntity("token-1", null));
            entityManager.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("CrudTokenRepository should enforce not null token value")
    void shouldDeleteToken() {
        final TokenEntity token = new TokenEntity("token-1", REFERENCE_DATE);

        tokenRepository.save(token);
        flushAndResetContext();

        tokenRepository.delete(token);
        flushAndResetContext();

        assertThat(tokenRepository.findById("token-1")).isEmpty();
    }

    @Test
    @DisplayName("CrudTokenRepository should delete all tokens when deleteAll is called")
    void shouldDeleteAllTokens() {
        tokenRepository.saveAll(List.of(new TokenEntity("token-1", REFERENCE_DATE),
                new TokenEntity("token-2", REFERENCE_DATE),
                new TokenEntity("token-3", REFERENCE_DATE)));
        flushAndResetContext();

        tokenRepository.deleteAll();
        flushAndResetContext();

        assertThat(StreamUtils.toList(tokenRepository.findAll()).size()).isEqualTo(0);
        assertThat(tokenRepository.existsById("token-1")).isEqualTo(false);
        assertThat(tokenRepository.existsById("token-2")).isEqualTo(false);
        assertThat(tokenRepository.existsById("token-3")).isEqualTo(false);
    }

    @Test
    @DisplayName("CrudTokenRepository should find all tokens when findAll is called")
    void shouldFindAllTokens() {
        tokenRepository.saveAll(List.of(new TokenEntity("token-1", REFERENCE_DATE),
                new TokenEntity("token-2", REFERENCE_DATE),
                new TokenEntity("token-3", REFERENCE_DATE)));
        flushAndResetContext();

        assertThat(StreamUtils.toList(tokenRepository.findAll()).size()).isEqualTo(3);
    }

    @Test
    @DisplayName("CrudTokenRepository should update token's expiration date when saving an existing token")
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

    @Test
    @DisplayName("CrudTokenRepository should throw when trying to save a token with null value")
    void shouldThrowWhenTokenValueIsNull() {
        assertThatThrownBy(() -> {
            tokenRepository.save(new TokenEntity(null, REFERENCE_DATE.plusDays(7)));
            entityManager.flush();
        }).isInstanceOf(JpaSystemException.class);
    }

    @Test
    @DisplayName("CrudTokenRepository should allow saving a token with an expiration date in the past")
    void shouldAllowTokenWithPastExpirationDate() {
        final TokenEntity expiredToken = new TokenEntity("expired-token", REFERENCE_DATE.minusDays(1));
        tokenRepository.save(expiredToken);
        flushAndResetContext();

        final TokenEntity retrieved = tokenRepository.findById("expired-token").orElseThrow();
        assertThat(retrieved.getExpirationDate()).isEqualTo(REFERENCE_DATE.minusDays(1));
    }

    private void flushAndResetContext() {
        entityManager.flush();
        entityManager.clear();
    }

}
