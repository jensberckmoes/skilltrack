package com.sopra_steria.jens_berckmoes;

import java.time.LocalDateTime;

public final class TestConstants {
    private TestConstants() {}

    public static final class Tokens {
        public static final String VALID_TOKEN = "valid_token";
        public static final String EXPIRED_TOKEN = "expired_token";
        public static final String WRONG_TOKEN = "wrong_token";
        public static final LocalDateTime STATIC_NOW = LocalDateTime.of(2026, 1, 30, 12, 15, 0);
    }

    public static final class Users {
        public static final String VALID_USERNAME = "jane.doe@example.com";
        public static final String EXPIRED_USERNAME = "old.user@example.com";
        public static final String INVALID_USERNAME = "nonexistent";
    }
}