package com.sopra_steria.jens_berckmoes.infra.repository;

import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;
import com.sopra_steria.jens_berckmoes.infra.mapping.UserMapper;
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
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("CrudUserRepository Integration Tests")
class CrudUserRepositoryTest {

    @Autowired private CrudUserRepository userRepository;

    @Autowired private CrudTokenRepository crudTokenRepository;

    @Autowired private EntityManager entityManager;

    @Test
    void shouldPersistUserWithTokenViaCascadeAll() {
        final UserEntity user = UserMapper.mapToInfra(VALID_USER);
        final Token userToken = VALID_USER.token();
        userRepository.save(user);
        flushAndResetContext();

        final UserEntity retrieved = userRepository.findById(VALID_USER.username()).orElseThrow();

        assertThat(retrieved.getUsername()).isEqualTo(VALID_USER.username());
        assertThat(retrieved.getToken().getValue()).isEqualTo(userToken.token());
        assertThat(retrieved.getToken().getExpirationDate()).isEqualTo(userToken.expirationDate());
    }

    @Test
    void shouldCascadeDeleteTokenWhenUserDeleted() {
        final UserEntity user = UserMapper.mapToInfra(VALID_USER);
        userRepository.save(user);
        flushAndResetContext();

        final String rawUserTokenString = VALID_USER.token().token();

        assertThat(crudTokenRepository.findById(rawUserTokenString)).isPresent();

        userRepository.deleteById(VALID_USER.username());
        flushAndResetContext();

        assertThat(crudTokenRepository.findById(rawUserTokenString)).isEmpty();
    }

    @Test
    @DisplayName("Should throw when username is null")
    void shouldThrowWhenUsernameIsNull() {
        final UserEntity user = UserMapper.mapToInfra(VALID_USER);
        user.setUsername(null);

        assertThatThrownBy(() -> {
            userRepository.save(user);
            entityManager.flush();
        }).isInstanceOf(JpaSystemException.class);
    }

    @Test
    @DisplayName("Should throw when token is null")
    void shouldThrowWhenTokenIsNull() {
        final UserEntity user = UserEntity.builder().username(VALID_USERNAME).token(null).build();

        assertThatThrownBy(() -> {
            userRepository.save(user);
            entityManager.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldDeleteAllTokens() {
        userRepository.saveAll(List.of(new UserEntity("user-1", new TokenEntity("token-1", REFERENCE_DATE)),
                new UserEntity("user-2", new TokenEntity("token-2", REFERENCE_DATE)),
                new UserEntity("user-3", new TokenEntity("token-3", REFERENCE_DATE))));
        flushAndResetContext();

        userRepository.deleteAll();
        flushAndResetContext();

        assertThat(StreamUtils.toList(userRepository.findAll()).size()).isEqualTo(0);
        assertThat(userRepository.existsById("token-1")).isEqualTo(false);
        assertThat(userRepository.existsById("token-2")).isEqualTo(false);
        assertThat(userRepository.existsById("token-3")).isEqualTo(false);
    }

    @Test
    void shouldFindAllTokens() {
        userRepository.saveAll(List.of(new UserEntity("user-1", new TokenEntity("token-1", REFERENCE_DATE)),
                new UserEntity("user-2", new TokenEntity("token-2", REFERENCE_DATE)),
                new UserEntity("user-3", new TokenEntity("token-3", REFERENCE_DATE))));
        flushAndResetContext();

        assertThat(StreamUtils.toList(userRepository.findAll()).size()).isEqualTo(3);
    }

    private void flushAndResetContext() {
        entityManager.flush();
        entityManager.clear();
    }
}
