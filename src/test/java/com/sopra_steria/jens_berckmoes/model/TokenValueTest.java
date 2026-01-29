package com.sopra_steria.jens_berckmoes.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class TokenValueTest {
    @Test
    void shouldThrowIfNull() {
        assertThatThrownBy(() -> TokenValue.of(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid token");
    }

    @Test
    void shouldThrowIfBlank() {
        assertThatThrownBy(() -> TokenValue.of("  "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldCreateValidTokenValue() {
        final String validTokenName = "some_valid_token";
        final TokenValue tokenValue = TokenValue.of(validTokenName);
        assertThat(tokenValue.value()).isEqualTo(validTokenName);
    }
}
