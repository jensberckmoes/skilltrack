package com.sopra_steria.jens_berckmoes.infra.repository;

import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CrudUserRepositoryTest {

    @Autowired
    private CrudUserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldSaveAndRetrieveToken() {
        final TokenEntity token = new TokenEntity("token-1", LocalDate.now().plusDays(7));
        final String userNameId = "user-1";
        final UserEntity user =  UserEntity.builder()
                .username(userNameId)
                .email("valid_email@email.com")
                .token(token)
                .build();
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        final UserEntity retrieved = userRepository.findById(userNameId).orElseThrow();
        assertThat(retrieved).isEqualTo(user);
    }

    @Test
    void shouldAllowUpdateEmail() {
        final TokenEntity token = new TokenEntity("token-1", LocalDate.now().plusDays(7));
        final String userNameId = "user-1";
        final UserEntity user = UserEntity.builder()
                .username(userNameId)
                .email("valid_email@email.com")
                .token(token)
                .build();
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        user.setEmail("another_valid_email@email.com");
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();


        final UserEntity retrieved = userRepository.findById(userNameId).orElseThrow();
        assertThat(retrieved.getEmail()).isEqualTo("another_valid_email@email.com");
    }

}
