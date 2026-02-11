package com.sopra_steria.jens_berckmoes.infra.repository;

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
import static com.sopra_steria.jens_berckmoes.TestConstants.TokenEntities.ALICE_TOKEN_ENTITY;
import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.ALICE_TOKEN;
import static com.sopra_steria.jens_berckmoes.TestConstants.UserEntities.ALICE_ENTITY;
import static com.sopra_steria.jens_berckmoes.TestConstants.UserEntities.USER_ENTITIES_AS_SET;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.*;
import static org.apache.logging.log4j.util.Strings.EMPTY;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("CrudUserRepository Integration Tests")
class CrudUserRepositoryTest {

    @Autowired
    private CrudUserRepository userRepository;

    @Autowired
    private CrudTokenRepository crudTokenRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("CrudUserRepository should save and retrieve user with token correctly via cascade all")
    void shouldPersistUserWithTokenViaCascadeAll() {
        userRepository.save(ALICE_ENTITY);
        flushAndResetContext();

        final UserEntity retrieved = userRepository.findById(ALICE.username()).orElseThrow();

        assertThat(retrieved.getUsername()).isEqualTo(ALICE.username());
        assertThat(retrieved.getToken().getValue()).isEqualTo(ALICE_TOKEN.token());
        assertThat(retrieved.getToken().getExpirationDate()).isEqualTo(ALICE_TOKEN.expirationDate());
    }

    @Test
    @DisplayName("Should cascade delete token when user is deleted")
    void shouldCascadeDeleteTokenWhenUserDeleted() {
        userRepository.save(ALICE_ENTITY);
        flushAndResetContext();

        assertThat(crudTokenRepository.findById(ALICE_TOKEN.token())).isPresent();

        userRepository.deleteById(ALICE.username());
        flushAndResetContext();

        assertThat(crudTokenRepository.findById(ALICE_TOKEN.token())).isEmpty();
    }

    @Test
    @DisplayName("Should throw when username is null")
    void shouldThrowWhenUsernameIsNull() {
        assertThatThrownBy(() -> {
            userRepository.save(UserEntity.builder().username(null).token(ALICE_TOKEN_ENTITY).build());
            entityManager.flush();
        }).isInstanceOf(JpaSystemException.class);
    }

    @Test
    @DisplayName("Should throw when token is null")
    void shouldThrowWhenTokenIsNull() {
        assertThatThrownBy(() -> {
            userRepository.save(UserEntity.builder().username(ALICE.username()).token(null).build());
            entityManager.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @ParameterizedTest
    @MethodSource("existByUsernameInParameters")
    @DisplayName("Should be able to check if users exist by username in a set of usernames")
    void existsByUsernameIn(final Set<String> usernames,
                            final boolean expectedResult) {
        userRepository.saveAll(USER_ENTITIES_AS_SET);
        flushAndResetContext();

        assertThat(userRepository.existsByUsernameIn(usernames)).isEqualTo(expectedResult);
    }

    public static Stream<Arguments> existByUsernameInParameters() {
        return Stream.of(Arguments.of(Set.of(EMPTY), false),
                Arguments.of(Set.of(BLANK), false),
                Arguments.of(null, false),
                Arguments.of(Set.of(NON_EXISTING_USERNAME_RAW_STRING), false),
                Arguments.of(Set.of(ALICE.username()), true),
                Arguments.of(Set.of(ALICE.username(), NON_EXISTING_USERNAME_RAW_STRING), true),
                Arguments.of(Set.of(DAVE.username(), NON_EXISTING_USERNAME_RAW_STRING), true),
                Arguments.of(Set.of(ALICE.username(), DAVE.username()), true),
                Arguments.of(Set.of(ALICE.username(), DAVE.username(), NON_EXISTING_USERNAME_RAW_STRING), true),
                Arguments.of(Set.of(ALICE.username(), DAVE.username(), CHARLIE.username()), true),
                Arguments.of(Set.of(ALICE.username(), DAVE.username(), CHARLIE.username(), NON_EXISTING_USERNAME_RAW_STRING), true));
    }

    @Test
    @DisplayName("Should delete all users and cascade delete all tokens when deleteAll is called")
    void shouldDeleteAllUsers() {
        userRepository.saveAll(USER_ENTITIES_AS_SET);
        flushAndResetContext();

        assertThat(userRepository.existsByUsernameIn(USERNAMES_AS_SET)).isTrue();

        userRepository.deleteAll();
        flushAndResetContext();

        assertThat(StreamUtils.toList(userRepository.findAll()).size()).isEqualTo(0);
        assertThat(userRepository.existsByUsernameIn(USERNAMES_AS_SET)).isFalse();
    }

    @Test
    @DisplayName("Should find all users when findAll is called")
    void shouldFindAllUsers() {
        userRepository.saveAll(USER_ENTITIES_AS_SET);
        flushAndResetContext();

        assertThat(StreamUtils.toList(userRepository.findAll()).size()).isEqualTo(USERS_AS_SET.size());
    }

    private void flushAndResetContext() {
        entityManager.flush();
        entityManager.clear();
    }


}
