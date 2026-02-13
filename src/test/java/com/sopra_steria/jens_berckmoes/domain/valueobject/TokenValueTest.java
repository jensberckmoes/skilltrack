package com.sopra_steria.jens_berckmoes.domain.valueobject;

import com.sopra_steria.jens_berckmoes.bdd.fakes.StepResult;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenRawValueNullOrBlankException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.sopra_steria.jens_berckmoes.TestConstants.BLANK;
import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.ALICE_TOKEN_RAW_STRING;
import static org.apache.logging.log4j.util.Strings.EMPTY;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Token value object")
class TokenValueTest {
    @Test
    @DisplayName("should create a valid token value when given a valid raw token")
    void shouldCreateValidTokenValue() {
        final StepResult<TokenValue> result = StepResult.callController(() -> TokenValue.of(ALICE_TOKEN_RAW_STRING));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.body().value()).isEqualTo(ALICE_TOKEN_RAW_STRING);
    }

    @ParameterizedTest
    @MethodSource("invalidRawTokensValues")
    @DisplayName("should throw TokenRawValueNullOrBlankException when given an invalid raw token")
    void shouldThrowIfInputIsInvalid(final String rawTokenValue, final String message) {
        final StepResult<TokenValue> result = StepResult.callController(() -> TokenValue.of(rawTokenValue));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.exception()).isInstanceOf(TokenRawValueNullOrBlankException.class);
        assertThat(result.exception().getMessage()).isEqualTo(message);
    }

    public static Stream<Arguments> invalidRawTokensValues() {
        return Stream.of(Arguments.of(null,"Token can not be null"),
                Arguments.of(EMPTY,"Token can not be blank"),
                Arguments.of(BLANK,"Token can not be blank"));
    }
}
