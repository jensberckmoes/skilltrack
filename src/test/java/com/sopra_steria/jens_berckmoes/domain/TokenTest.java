package com.sopra_steria.jens_berckmoes.domain;

import com.sopra_steria.jens_berckmoes.domain.exception.TokenRawValueNullOrBlankException;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenExpirationDateNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static com.sopra_steria.jens_berckmoes.TestConstants.BLANK;
import static com.sopra_steria.jens_berckmoes.TestConstants.TimeFixture.*;
import static org.apache.logging.log4j.util.Strings.EMPTY;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("Domain Token")
class TokenTest {
    @ParameterizedTest
    @MethodSource("invalidTokens")
    @DisplayName("should throw TokenRawValueNullOrBlankException when given an invalid raw token")
    void shouldThrowIfInputIsInvalid(final String token, final LocalDate expirationDate) {
        assertThatThrownBy(() -> Token.of(token, expirationDate))
                .isInstanceOf(TokenRawValueNullOrBlankException.class);
    }

    @Test
    @DisplayName("should throw TokenExpirationDateNullException when given a null expiration date")
    void shouldThrowIfValidUntilIsInvalid() {
        assertThatThrownBy(() -> Token.of("t", null))
                .isInstanceOf(TokenExpirationDateNullException.class);
    }

    public static Stream<Arguments> invalidTokens() {
        final LocalDate validTokenOneDay = DATE_TOMORROW;
        return Stream.of(
                Arguments.of(null, validTokenOneDay),
                Arguments.of(EMPTY, validTokenOneDay),
                Arguments.of(BLANK, validTokenOneDay));
    }

    @ParameterizedTest
    @MethodSource("datesProvider")
    @DisplayName("should determine whether a token is expired at a given date correctly using the isExpiredAt method")
    void shouldDetermineExpirationCorrectly(final LocalDate referenceDate, final boolean expectedExpired) {
        final Token givenToken = Token.of("t", REFERENCE_DATE);
        assertThat(givenToken.isExpiredAt(referenceDate)).isEqualTo(expectedExpired);
    }

    public static Stream<Arguments> datesProvider() {
        return Stream.of(
                Arguments.of(DATE_TOMORROW, true),
                Arguments.of(REFERENCE_DATE, false),
                Arguments.of(DATE_YESTERDAY, false));
    }
}
