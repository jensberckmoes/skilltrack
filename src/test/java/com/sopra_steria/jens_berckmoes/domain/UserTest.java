package com.sopra_steria.jens_berckmoes.domain;

import com.sopra_steria.jens_berckmoes.bdd.fakes.StepResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.*;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Domain User")
class UserTest {

    @ParameterizedTest
    @MethodSource("belongsToProvider")
    @DisplayName("should determine whether a token belongs to the user correctly using the ownsToken method")
    void shouldDetermineBelongsToCorrectly(final User givenUser,
                                           final Token givenToken,
                                           final boolean expectedBelongsTo) {
        final boolean actual = givenUser.ownsToken(givenToken);

        assertThat(actual).isEqualTo(expectedBelongsTo);
    }

    public static Stream<Arguments> belongsToProvider() {
        return Stream.of(
                Arguments.of(ALICE, ALICE_TOKEN, true),
                Arguments.of(ALICE, BOB_TOKEN, false),
                Arguments.of(BOB, BOB_TOKEN, true),
                Arguments.of(CHARLIE, BOB_TOKEN, false),
                Arguments.of(DAVE, ALICE_TOKEN, false));
    }
}