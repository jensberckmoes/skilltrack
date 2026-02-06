package com.sopra_steria.jens_berckmoes.infra.repository;

import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;
import com.sopra_steria.jens_berckmoes.util.StreamUtils;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;
import java.util.stream.Stream;

import static com.sopra_steria.jens_berckmoes.TestConstants.BLANK;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.*;
import static com.sopra_steria.jens_berckmoes.infra.mapping.UserMapper.mapToInfra;
import static org.apache.logging.log4j.util.Strings.EMPTY;
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
    @DisplayName("CrudUserRepository should save and retrieve user with token correctly via cascade all")
    void shouldPersistUserWithTokenViaCascadeAll() {
        final UserEntity user = mapToInfra(VALID_USER);
        final Token userToken = VALID_USER.token();
        userRepository.save(user);
        flushAndResetContext();

        final UserEntity retrieved = userRepository.findById(VALID_USER.username()).orElseThrow();

        assertThat(retrieved.getUsername()).isEqualTo(VALID_USER.username());
        assertThat(retrieved.getToken().getValue()).isEqualTo(userToken.token());
        assertThat(retrieved.getToken().getExpirationDate()).isEqualTo(userToken.expirationDate());
    }

    @Test
    @DisplayName("Should cascade delete token when user is deleted")
    void shouldCascadeDeleteTokenWhenUserDeleted() {
        final UserEntity user = mapToInfra(VALID_USER);
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
        final UserEntity user = mapToInfra(VALID_USER);
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

    @ParameterizedTest
    @MethodSource("existByUsernameInParameters")
    @DisplayName("Should be able to check if users exist by username in a set of usernames")
    void existsByUsernameIn(final Set<String> usernames, final boolean expectedResult) {
        userRepository.saveAll(mapToInfra(TEST_USERS));
        flushAndResetContext();

        assertThat(userRepository.existsByUsernameIn(usernames)).isEqualTo(expectedResult);
    }

    public static Stream<Arguments> existByUsernameInParameters() {
        return Stream.of(Arguments.of(Set.of(EMPTY), false),
                Arguments.of(Set.of(BLANK), false),
                Arguments.of(null, false),
                Arguments.of(Set.of("-"), false),
                Arguments.of(Set.of(VALID_USERNAME), true),
                Arguments.of(Set.of(VALID_USERNAME, "-"), true),
                Arguments.of(Set.of(SECOND_VALID_USERNAME, "-"), true),
                Arguments.of(Set.of(VALID_USERNAME, SECOND_VALID_USERNAME), true),
                Arguments.of(Set.of(VALID_USERNAME, SECOND_VALID_USERNAME, "-"), true),
                Arguments.of(Set.of(VALID_USERNAME, SECOND_VALID_USERNAME, EXPIRED_USERNAME), true),
                Arguments.of(Set.of(VALID_USERNAME, SECOND_VALID_USERNAME, EXPIRED_USERNAME, "-"), true));
    }

    @Test
    @DisplayName("Should delete all users and cascade delete all tokens when deleteAll is called")
    void shouldDeleteAllUsers() {
        userRepository.saveAll(mapToInfra(TEST_USERS));
        flushAndResetContext();

        assertThat(userRepository.existsByUsernameIn(USER_KEYS)).isTrue();

        userRepository.deleteAll();
        flushAndResetContext();

        assertThat(StreamUtils.toList(userRepository.findAll()).size()).isEqualTo(0);
        assertThat(userRepository.existsByUsernameIn(USER_KEYS)).isFalse();
    }

    @Test
    @DisplayName("Should find all users when findAll is called")
    void shouldFindAllUsers() {
        userRepository.saveAll(mapToInfra(TEST_USERS));
        flushAndResetContext();

        assertThat(StreamUtils.toList(userRepository.findAll()).size()).isEqualTo(3);
    }

    private void flushAndResetContext() {
        entityManager.flush();
        entityManager.clear();
    }


}
