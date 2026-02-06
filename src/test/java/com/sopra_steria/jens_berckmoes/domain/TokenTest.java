package com.sopra_steria.jens_berckmoes.domain;

import com.sopra_steria.jens_berckmoes.domain.exception.TokenExpirationDateNullException;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenRawValueNullOrBlankException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static com.sopra_steria.jens_berckmoes.TestConstants.BLANK;
import static com.sopra_steria.jens_berckmoes.TestConstants.TimeFixture.*;
import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.VALID_TOKEN_FOR_ONE_MORE_DAY;
import static org.apache.logging.log4j.util.Strings.EMPTY;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("Domain Token")
class TokenTest {
    @Test
    @DisplayName("should throw TokenExpirationDateNullException when given a null expiration date")
    void shouldThrowIfValidUntilIsInvalid() {
        assertThatThrownBy(() -> Token.of("-", null)).isInstanceOf(TokenExpirationDateNullException.class);
    }

    @ParameterizedTest
    @MethodSource("invalidTokens")
    @DisplayName("should throw TokenRawValueNullOrBlankException when given an invalid raw token")
    void shouldThrowIfInputIsInvalid(final String token) {
        assertThatThrownBy(() -> Token.of(token, TEST_TOMORROW)).isInstanceOf(TokenRawValueNullOrBlankException.class);
    }

    public static Stream<Arguments> invalidTokens() {
        return Stream.of(Arguments.of((Object) null), Arguments.of(EMPTY), Arguments.of(BLANK));
    }

    @ParameterizedTest
    @MethodSource("datesProvider")
    @DisplayName("should determine whether a token is expired at a given date correctly using the isExpiredAt method")
    void shouldDetermineExpirationCorrectly(final LocalDate referenceDate, final boolean expectedExpired) {
        assertThat(VALID_TOKEN_FOR_ONE_MORE_DAY.isExpiredAt(referenceDate)).isEqualTo(expectedExpired);
    }

    public static Stream<Arguments> datesProvider() {
        return Stream.of(Arguments.of(TEST_TOMORROW.plusDays(1), true),
                Arguments.of(TEST_TODAY, false),
                Arguments.of(TEST_YESTERDAY, false));
    }

}
