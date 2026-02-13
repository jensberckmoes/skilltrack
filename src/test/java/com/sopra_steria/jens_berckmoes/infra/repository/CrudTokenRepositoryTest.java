package com.sopra_steria.jens_berckmoes.infra.repository;

import com.sopra_steria.jens_berckmoes.domain.Token;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sopra_steria.jens_berckmoes.TestConstants.BLANK;
import static com.sopra_steria.jens_berckmoes.TestConstants.TestMethods.flushAndResetContext;
import static com.sopra_steria.jens_berckmoes.TestConstants.TimeFixture.TEST_TODAY;
import static com.sopra_steria.jens_berckmoes.TestConstants.TokenEntities.ALICE_TOKEN_ENTITY;
import static com.sopra_steria.jens_berckmoes.TestConstants.TokenEntities.TOKEN_ENTITIES_AS_SET;
import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.*;
import static org.apache.logging.log4j.util.Strings.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


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
        flushAndResetContext(() -> tokenRepository.save(ALICE_TOKEN_ENTITY), entityManager);

        final TokenEntity retrieved = tokenRepository.findById(ALICE_TOKEN.token()).orElseThrow();
        assertThat(retrieved).isEqualTo(ALICE_TOKEN_ENTITY);
    }

    @Test
    @DisplayName("should enforce not null expiration date")
    void shouldEnforceNotNullExpirationDate() {
        assertThatThrownBy(() -> flushAndResetContext(() -> tokenRepository.save(new TokenEntity("token-1", null)), entityManager)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("should be able to delete the token when delete is called")
    void shouldDeleteToken() {
        flushAndResetContext(() -> tokenRepository.save(ALICE_TOKEN_ENTITY), entityManager);
        flushAndResetContext(() -> tokenRepository.delete(ALICE_TOKEN_ENTITY), entityManager);

        assertThat(tokenRepository.findById(ALICE_TOKEN.token())).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("existByTokenInParameters")
    @DisplayName("Should be able to check if token exists by value in a set of values")
    void existsByTokenValueIn(final Set<String> values, final boolean expectedResult) {
        flushAndResetContext(() -> tokenRepository.saveAll(TOKEN_ENTITIES_AS_SET), entityManager);

        assertThat(tokenRepository.existsByValueIn(values)).isEqualTo(expectedResult);
    }

    public static Stream<Arguments> existByTokenInParameters() {
        return Stream.of(Arguments.of(Set.of(EMPTY), false),
                Arguments.of(Set.of(), false),
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
        flushAndResetContext(() -> tokenRepository.saveAll(TOKEN_ENTITIES_AS_SET), entityManager);

        final Set<String> TOKEN_VALUES_AS_SET = BDD_TOKENS_WITH_REALISTIC_VALUES.values().stream().map(Token::token).collect(Collectors.toSet());
        assertThat(tokenRepository.existsByValueIn(TOKEN_VALUES_AS_SET)).isTrue();

        flushAndResetContext(() -> tokenRepository.deleteAll(), entityManager);

        assertThat(tokenRepository.existsByValueIn(TOKEN_VALUES_AS_SET)).isFalse();
    }

    @Test
    @DisplayName("should find all tokens when findAll is called")
    void shouldFindAllTokens() {
        flushAndResetContext(() -> tokenRepository.saveAll(TOKEN_ENTITIES_AS_SET), entityManager);

        assertThat(StreamUtils.toList(tokenRepository.findAll())).hasSize(BDD_TOKENS_WITH_REALISTIC_VALUES.size());
    }

    @Test
    @DisplayName("should update token's expiration date when saving an existing token")
    void shouldUpdateToken() {
        flushAndResetContext(() -> tokenRepository.save(ALICE_TOKEN_ENTITY), entityManager);

        final TokenEntity retrieved = tokenRepository.findById(ALICE_TOKEN_ENTITY.getValue()).orElseThrow();
        final int amountOfDaysToUpdate = 14;
        retrieved.setExpirationDate(retrieved.getExpirationDate().plusDays(amountOfDaysToUpdate));

        flushAndResetContext(() -> tokenRepository.save(retrieved), entityManager);

        final TokenEntity updated = tokenRepository.findById(ALICE_TOKEN_ENTITY.getValue()).orElseThrow();
        assertThat(updated.getExpirationDate()).isEqualTo(ALICE_TOKEN_ENTITY.getExpirationDate().plusDays(amountOfDaysToUpdate));
    }

    @Test
    @DisplayName("should throw an exception when trying to save a token with null value")
    void shouldThrowWhenTokenValueIsNull() {
        assertThatThrownBy(() ->
                flushAndResetContext(() ->
                        tokenRepository.save(new TokenEntity(null, TEST_TODAY.plusDays(7))), entityManager))
                .isInstanceOf(JpaSystemException.class);
    }


}
