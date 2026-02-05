package com.sopra_steria.jens_berckmoes.domain.valueobject;

import com.sopra_steria.jens_berckmoes.TestConstants;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenRawValueNullOrBlankException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class TokenValueTest {
    @Test
    @DisplayName("TokenValue should create a valid token value when given a valid raw token")
    void shouldCreateValidTokenValue() {
        final String validTokenName = TestConstants.Tokens.VALID_RAW_TOKEN;
        final TokenValue tokenValue = TokenValue.of(validTokenName);
        assertThat(tokenValue.value()).isEqualTo(validTokenName);
    }

    @ParameterizedTest
    @MethodSource("invalidRawTokensValues")
    @DisplayName("TokenValue should throw TokenRawValueNullOrBlankException when given an invalid raw token")
    void shouldThrowIfInputIsInvalid(final String rawTokenValue) {
        assertThatThrownBy(() -> TokenValue.of(rawTokenValue))
                .isInstanceOf(TokenRawValueNullOrBlankException.class);
    }

    public static Stream<Arguments> invalidRawTokensValues() {
        return Stream.of(
                Arguments.of((String) null),//cast for confused varargs
                Arguments.of(""),
                Arguments.of("  "));
    }
}
