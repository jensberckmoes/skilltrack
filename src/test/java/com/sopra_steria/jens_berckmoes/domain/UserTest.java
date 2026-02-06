package com.sopra_steria.jens_berckmoes.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.sopra_steria.jens_berckmoes.TestConstants.TimeFixture.TEST_TODAY;
import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.VALID_TOKEN_FOR_TEN_YEARS;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.VALID_USER_FOR_TEN_YEAR;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Domain User")
class UserTest {

    @ParameterizedTest
    @MethodSource("belongsToProvider")
    @DisplayName("should determine whether a token belongs to the user correctly using the ownsToken method")
    void shouldDetermineBelongsToCorrectly(final Token givenToken,
                                           final User givenUser,
                                           final boolean expectedBelongsTo) {
        assertThat(givenUser.ownsToken(givenToken)).isEqualTo(expectedBelongsTo);
    }

    public static Stream<Arguments> belongsToProvider() {
        return Stream.of(Arguments.of(VALID_TOKEN_FOR_TEN_YEARS, VALID_USER_FOR_TEN_YEAR, true),
                Arguments.of(VALID_TOKEN_FOR_TEN_YEARS, User.of("-", Token.of("-", TEST_TODAY)), false));
    }
}