package com.sopra_steria.jens_berckmoes.infra.repository;

import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
class CrudTokenRepositoryTest {

    @Autowired
    private CrudTokenRepository tokenRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldSaveAndRetrieveToken() {
        final TokenEntity token = new TokenEntity("token-1", LocalDate.now().plusDays(7));
        tokenRepository.save(token);
        entityManager.flush();
        entityManager.clear();

        final TokenEntity retrieved = tokenRepository.findById("token-1").orElseThrow();
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
        final TokenEntity token = new TokenEntity("token-1", LocalDate.now());
        tokenRepository.save(token);

        token.setExpirationDate(LocalDate.now().plusDays(14));
        tokenRepository.save(token);
        entityManager.flush();
        entityManager.clear();

        final TokenEntity retrieved = tokenRepository.findById("token-1").orElseThrow();
        assertThat(retrieved.getExpirationDate()).isEqualTo(LocalDate.now().plusDays(14));
    }

}
