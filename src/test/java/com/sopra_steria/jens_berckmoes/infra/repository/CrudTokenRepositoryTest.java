package com.sopra_steria.jens_berckmoes.infra.repository;

import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import jakarta.persistence.Id;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CrudTokenRepositoryTest {

    @Autowired
    private CrudTokenRepository tokenRepository;

    @Test
    void shouldSaveAndRetrieveTokenEntity() {
        final TokenEntity tokenToSave = TokenEntity.builder().value("hashed-tokenToSave").expirationDate(LocalDateTime.now()).build();

        final TokenEntity saved = tokenRepository.save(tokenToSave);
        final TokenEntity retrieved = tokenRepository.findById("hashed-tokenToSave").orElseThrow();

        assertThat(retrieved).isEqualTo(saved);
    }

    @Test
    void tokenHasIdAnnotation() throws NoSuchFieldException {
        final Id id = TokenEntity.class.getDeclaredField("value").getAnnotation(Id.class);
        assertThat(id).isNotNull();
    }

}
