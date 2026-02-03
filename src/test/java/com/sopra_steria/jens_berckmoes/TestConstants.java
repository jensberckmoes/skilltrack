package com.sopra_steria.jens_berckmoes;

import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.valueobject.TokenValue;
import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.valueobject.Username;

import java.time.LocalDateTime;
import java.util.Map;

import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.EXPIRED_TOKEN;
import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.VALID_TOKEN;

public final class TestConstants {
    private TestConstants() {
    }

    public static final class Tokens {
        public static final String VALID_TOKEN = "valid_token";
        public static final String EXPIRED_TOKEN = "expired_token";
        public static final String WRONG_TOKEN = "wrong_token";
        public static final LocalDateTime STATIC_NOW = LocalDateTime.of(2026, 1, 30, 12, 15, 0);
        public static final Map<TokenValue, Token> TEST_TOKENS = Map.ofEntries(
                Map.entry(TokenValue.of(VALID_TOKEN), Token.of(TokenValue.of(VALID_TOKEN).value(), LocalDateTime.of(2027, 1, 30, 16, 19, 0).plusDays(1))),
                Map.entry(TokenValue.of(EXPIRED_TOKEN), Token.of(TokenValue.of(EXPIRED_TOKEN).value(), LocalDateTime.of(2027, 1, 30, 16, 19, 0).minusYears(10))));

    }

    public static final class Users {
        public static final String VALID_USERNAME = "jane.doe@example.com";
        public static final String EXPIRED_USERNAME = "old.user@example.com";
        public static final String INVALID_USERNAME = "nonexistent";
        public static final Map<Username, User> TEST_USERS = Map.ofEntries(
                Map.entry(Username.of(VALID_USERNAME), User.of(Username.of(VALID_USERNAME), TestConstants.Tokens.TEST_TOKENS.get(TokenValue.of(VALID_TOKEN)))),
                Map.entry(Username.of(EXPIRED_USERNAME), User.of(Username.of(EXPIRED_USERNAME), TestConstants.Tokens.TEST_TOKENS.get(TokenValue.of(EXPIRED_TOKEN)))));
    }
}