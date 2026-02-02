package com.sopra_steria.jens_berckmoes.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.STATIC_NOW;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserTest {

    @ParameterizedTest
    @MethodSource("belongsToProvider")
    void shouldDetermineBelongsToCorrectly(final Token givenToken, final User givenUser, final boolean expectedBelongsTo) {
        assertThat(givenUser.ownsToken(givenToken)).isEqualTo(expectedBelongsTo);
    }

    public static Stream<Arguments> belongsToProvider() {
        final Token candidateToken = Token.of("t", STATIC_NOW);
        final Username givenUserUsername = Username.of("u");
        return Stream.of(
                Arguments.of(candidateToken, User.of(givenUserUsername, candidateToken), true),
                Arguments.of(candidateToken, User.of(givenUserUsername, Token.of("s", STATIC_NOW)), false));
    }
}