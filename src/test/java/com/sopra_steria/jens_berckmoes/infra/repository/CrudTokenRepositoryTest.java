package com.sopra_steria.jens_berckmoes.infra.repository;

import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
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
import static com.sopra_steria.jens_berckmoes.TestConstants.TimeFixture.TEST_TODAY;
import static com.sopra_steria.jens_berckmoes.TestConstants.TimeFixture.TEST_YESTERDAY;
import static com.sopra_steria.jens_berckmoes.TestConstants.TokenEntities.*;
import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.*;
import static com.sopra_steria.jens_berckmoes.TestConstants.UserEntities.ALICE_ENTITY;
import static org.apache.logging.log4j.util.Strings.EMPTY;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("CrudTokenRepository")
class CrudTokenRepositoryTest {

    @Autowired
    private CrudTokenRepository tokenRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("should save and retrieve token correctly")
    void shouldSaveAndRetrieveToken() {
        tokenRepository.save(ALICE_TOKEN_ENTITY);
        flushAndResetContext();

        final TokenEntity retrieved = tokenRepository.findById(ALICE_TOKEN.token()).orElseThrow();
        assertThat(retrieved).isEqualTo(ALICE_TOKEN_ENTITY);
    }

    @Test
    @DisplayName("should enforce not null expiration date")
    void shouldEnforceNotNullExpirationDate() {
        assertThatThrownBy(() -> {
            tokenRepository.save(new TokenEntity("token-1", null));
            entityManager.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("should be able to delete the token when delete is called")
    void shouldDeleteToken() {
        tokenRepository.save(ALICE_TOKEN_ENTITY);
        flushAndResetContext();

        tokenRepository.delete(ALICE_TOKEN_ENTITY);
        flushAndResetContext();

        assertThat(tokenRepository.findById(ALICE_TOKEN.token())).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("existByTokenInParameters")
    @DisplayName("Should be able to check if token exists by value in a set of values")
    void existsByTokenValueIn(final Set<String> values, final boolean expectedResult) {
        tokenRepository.saveAll(TOKEN_ENTITIES_AS_SET);
        flushAndResetContext();

        assertThat(tokenRepository.existsByValueIn(values)).isEqualTo(expectedResult);
    }

    public static Stream<Arguments> existByTokenInParameters() {
        return Stream.of(Arguments.of(Set.of(EMPTY), false),
                Arguments.of(Set.of(BLANK), false),
                Arguments.of(Set.of(NON_EXISTING_TOKEN_RAW_STRING), false),
                Arguments.of(null, false),
                Arguments.of(Set.of(ALICE_TOKEN.token()), true),
                Arguments.of(Set.of(ALICE_TOKEN.token(), NON_EXISTING_TOKEN_RAW_STRING), true),
                Arguments.of(Set.of(DAVE_TOKEN.token(), NON_EXISTING_TOKEN_RAW_STRING), true),
                Arguments.of(Set.of(ALICE_TOKEN.token(), DAVE_TOKEN.token()),
                        true),
                Arguments.of(Set.of(ALICE_TOKEN.token(), DAVE_TOKEN.token(), NON_EXISTING_TOKEN_RAW_STRING),
                        true),
                Arguments.of(Set.of(ALICE_TOKEN.token(),
                        DAVE_TOKEN.token(),
                        BOB_TOKEN.token()), true),
                Arguments.of(Set.of(ALICE_TOKEN.token(),
                        DAVE_TOKEN.token(),
                        BOB_TOKEN.token(),
                        NON_EXISTING_TOKEN_RAW_STRING), true));
    }

    @Test
    @DisplayName("should delete all tokens when deleteAll is called")
    void shouldDeleteAllTokens() {
        tokenRepository.saveAll(TOKEN_ENTITIES_AS_SET);
        flushAndResetContext();

        assertThat(tokenRepository.existsByValueIn(TOKEN_VALUES_AS_SET)).isTrue();

        tokenRepository.deleteAll();
        flushAndResetContext();

        assertThat(tokenRepository.existsByValueIn(TOKEN_VALUES_AS_SET)).isFalse();
    }

    @Test
    @DisplayName("should find all tokens when findAll is called")
    void shouldFindAllTokens() {
        tokenRepository.saveAll(TOKEN_ENTITIES_AS_SET);
        flushAndResetContext();

        assertThat(StreamUtils.toList(tokenRepository.findAll()).size()).isEqualTo(TOKENS_AS_SET.size());
    }

    @Test
    @DisplayName("should update token's expiration date when saving an existing token")
    void shouldUpdateToken() {
        tokenRepository.save(ALICE_TOKEN_ENTITY);
        flushAndResetContext();

        final TokenEntity retrieved = tokenRepository.findById(ALICE_TOKEN_ENTITY.getValue()).orElseThrow();
        final int amountOfDaysToUpdate = 14;
        retrieved.setExpirationDate(retrieved.getExpirationDate().plusDays(amountOfDaysToUpdate));
        tokenRepository.save(retrieved);
        flushAndResetContext();

        final TokenEntity updated = tokenRepository.findById(ALICE_TOKEN_ENTITY.getValue()).orElseThrow();
        assertThat(updated.getExpirationDate()).isEqualTo(ALICE_TOKEN_ENTITY.getExpirationDate().plusDays(amountOfDaysToUpdate));
    }

    @Test
    @DisplayName("should throw an exception when trying to save a token with null value")
    void shouldThrowWhenTokenValueIsNull() {
        assertThatThrownBy(() -> {
            tokenRepository.save(new TokenEntity(null, TEST_TODAY.plusDays(7)));
            entityManager.flush();
        }).isInstanceOf(JpaSystemException.class);
    }

    @Test
    @DisplayName("should allow saving a token with an expiration date in the past")
    void shouldAllowTokenWithPastExpirationDate() {
        tokenRepository.save(BOB_TOKEN_ENTITY);
        flushAndResetContext();

        final TokenEntity retrieved = tokenRepository.findById(BOB_TOKEN.token()).orElseThrow();
        assertThat(retrieved.getExpirationDate()).isEqualTo(TEST_YESTERDAY);
    }

    private void flushAndResetContext() {
        entityManager.flush();
        entityManager.clear();
    }

}
