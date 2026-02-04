package com.sopra_steria.jens_berckmoes;

import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.valueobject.TokenValue;
import com.sopra_steria.jens_berckmoes.domain.valueobject.Username;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestConstants {

    public static final class TimeFixture {
        public static final LocalDate REFERENCE_DATE = LocalDate.of(2026, 1, 30);

        public static final LocalDate DATE_FAR_FUTURE = REFERENCE_DATE.plusYears(10);
        public static final LocalDate DATE_FAR_PAST = REFERENCE_DATE.minusYears(10);
        public static final LocalDate DATE_TOMORROW = REFERENCE_DATE.plusDays(1);
        public static final LocalDate DATE_YESTERDAY = REFERENCE_DATE.minusDays(1);
    }

    public static final class Tokens {
        public static final String VALID_RAW_TOKEN = "valid_token";
        public static final String SECOND_VALID_RAW_TOKEN = "valid_token-2";
        public static final String EXPIRED_RAW_TOKEN = "expired_token";
        public static final String WRONG_RAW_TOKEN = "wrong_token";

        public static final Token VALID_TOKEN = Token.of(VALID_RAW_TOKEN, TimeFixture.DATE_FAR_FUTURE);
        public static final Token SECOND_VALID_TOKEN = Token.of(SECOND_VALID_RAW_TOKEN, TimeFixture.DATE_FAR_FUTURE.plusDays(1));
        public static final Token EXPIRED_TOKEN = Token.of(EXPIRED_RAW_TOKEN, TimeFixture.DATE_FAR_PAST);

        public static final Map<TokenValue, Token> TEST_TOKENS = Map.ofEntries(
                Map.entry(TokenValue.of(VALID_RAW_TOKEN), VALID_TOKEN),
                Map.entry(TokenValue.of(SECOND_VALID_RAW_TOKEN), SECOND_VALID_TOKEN),
                Map.entry(TokenValue.of(EXPIRED_RAW_TOKEN), EXPIRED_TOKEN));

    }

    public static final class Users {
        public static final String VALID_USERNAME = "jane.doe@example.com";
        public static final String SECOND_VALID_USERNAME = "john.doe@example.com";
        public static final String EXPIRED_USERNAME = "old.user@example.com";
        public static final String INVALID_USERNAME = "nonexistent";

        public static final User VALID_USER = User.of(Username.of(VALID_USERNAME), TestConstants.Tokens.TEST_TOKENS.get(TokenValue.of(VALID_RAW_TOKEN)));
        public static final User SECOND_VALID_USER = User.of(Username.of(SECOND_VALID_USERNAME), TestConstants.Tokens.TEST_TOKENS.get(TokenValue.of(SECOND_VALID_RAW_TOKEN)));
        public static final User EXPIRED_USER = User.of(Username.of(EXPIRED_USERNAME), TestConstants.Tokens.TEST_TOKENS.get(TokenValue.of(EXPIRED_RAW_TOKEN)));

        public static final Map<Username, User> TEST_USERS = Map.ofEntries(
                Map.entry(Username.of(VALID_USERNAME), VALID_USER),
                Map.entry(Username.of(SECOND_VALID_USERNAME), SECOND_VALID_USER),
                Map.entry(Username.of(EXPIRED_USERNAME), EXPIRED_USER));
    }
}