package com.sopra_steria.jens_berckmoes.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class TokenTest {

    @ParameterizedTest
    @MethodSource("provideInputForInputIsNullOrBlank")
    void shouldThrowIfInputIsInvalid(final String token, final LocalDateTime validUntil, final String valueUnderTest) {
        assertThatThrownBy(() -> Token.of(token, validUntil))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(String.format("Token %s cannot be null", valueUnderTest));
    }

    @ParameterizedTest
    @MethodSource("provideInputForInputWeeklyIsNullOrBlank")
    void shouldThrowIfInputIsInvalidForWeekly(final String token, final String valueUnderTest) {
        assertThatThrownBy(() -> Token.weeklyToken(token))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(String.format("Token %s cannot be null", valueUnderTest));
    }

    public static Stream<Arguments> provideInputForInputIsNullOrBlank() {
        final LocalDateTime timestamp = LocalDateTime.now().plusDays(1);
        final String tokenValue = "value";
        return Stream.of(
                Arguments.of(null, timestamp, tokenValue),
                Arguments.of("", timestamp, tokenValue),
                Arguments.of("  ", timestamp, tokenValue),
                Arguments.of("abc", null, "validUntil"));
    }

    public static Stream<Arguments> provideInputForInputWeeklyIsNullOrBlank() {
        final String tokenValue = "value";
        return Stream.of(
                Arguments.of(null, tokenValue),
                Arguments.of("", tokenValue),
                Arguments.of("  ", tokenValue));
    }

    @Test
    void shouldHaveExpired() {
        final Token expired = Token.of("t", LocalDateTime.now().minusHours(1));
        assertThat(expired.hasExpired()).isTrue();
    }

    @Test
    void shouldNotHaveExpired() {
        final Token expired = Token.of("t", LocalDateTime.now().plusHours(1));
        assertThat(expired.hasExpired()).isFalse();
    }
}
