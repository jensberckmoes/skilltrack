package com.sopra_steria.jens_berckmoes.domain.valueobject;

import com.sopra_steria.jens_berckmoes.domain.exception.TokenRawValueNullOrBlankException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.sopra_steria.jens_berckmoes.TestConstants.BLANK;
import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.VALID_TOKEN_FOR_TEN_YEARS_RAW_STRING;
import static org.apache.logging.log4j.util.Strings.EMPTY;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("Token value object")
class TokenValueTest {
    @Test
    @DisplayName("should create a valid token value when given a valid raw token")
    void shouldCreateValidTokenValue() {
        assertThat(TokenValue.of(VALID_TOKEN_FOR_TEN_YEARS_RAW_STRING).value()).isEqualTo(
                VALID_TOKEN_FOR_TEN_YEARS_RAW_STRING);
    }

    @ParameterizedTest
    @MethodSource("invalidRawTokensValues")
    @DisplayName("should throw TokenRawValueNullOrBlankException when given an invalid raw token")
    void shouldThrowIfInputIsInvalid(final String rawTokenValue) {
        assertThatThrownBy(() -> TokenValue.of(rawTokenValue)).isInstanceOf(TokenRawValueNullOrBlankException.class);
    }

    public static Stream<Arguments> invalidRawTokensValues() {
        return Stream.of(Arguments.of((String) null),//cast for confused varargs
                Arguments.of(EMPTY), Arguments.of(BLANK));
    }
}
