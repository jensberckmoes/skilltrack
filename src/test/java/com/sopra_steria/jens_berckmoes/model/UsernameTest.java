package com.sopra_steria.jens_berckmoes.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class UsernameTest {

    @Test
    void shouldThrowIfNull() {
        assertThatThrownBy(() -> Username.of(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid username");
    }

    @Test
    void shouldThrowIfBlank() {
        assertThatThrownBy(() -> Username.of("  "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldCreateValidUsername() {
        Username username = Username.of("jane.doe@example.com");
        assertThat(username.value()).isEqualTo("jane.doe@example.com");
    }
}
