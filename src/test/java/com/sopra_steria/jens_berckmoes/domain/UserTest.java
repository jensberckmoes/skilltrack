package com.sopra_steria.jens_berckmoes.domain;

import com.sopra_steria.jens_berckmoes.domain.valueobject.Username;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.sopra_steria.jens_berckmoes.TestConstants.TimeFixture.REFERENCE_DATE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserTest {

    @ParameterizedTest
    @MethodSource("belongsToProvider")
    void shouldDetermineBelongsToCorrectly(final Token givenToken, final User givenUser, final boolean expectedBelongsTo) {
        assertThat(givenUser.ownsToken(givenToken)).isEqualTo(expectedBelongsTo);
    }

    public static Stream<Arguments> belongsToProvider() {
        final Token candidateToken = Token.of("t", REFERENCE_DATE);
        final Username givenUserUsername = Username.of("u");
        return Stream.of(
                Arguments.of(candidateToken, User.of(givenUserUsername, candidateToken), true),
                Arguments.of(candidateToken, User.of(givenUserUsername, Token.of("s", REFERENCE_DATE)), false));
    }
}