package com.sopra_steria.jens_berckmoes;

import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestConstants {

    public static final String BLANK = " ";

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

        public static final Token VALID_TOKEN = Token.of(VALID_RAW_TOKEN, TimeFixture.DATE_FAR_FUTURE);
        public static final Token SECOND_VALID_TOKEN = Token.of(SECOND_VALID_RAW_TOKEN,
                TimeFixture.DATE_FAR_FUTURE.plusDays(1));
        public static final Token EXPIRED_TOKEN = Token.of(EXPIRED_RAW_TOKEN, TimeFixture.DATE_FAR_PAST);

        public static final Map<String, Token> TOKEN_DICTIONARY = Map.ofEntries(Map.entry(VALID_RAW_TOKEN, VALID_TOKEN),
                Map.entry(SECOND_VALID_RAW_TOKEN, SECOND_VALID_TOKEN),
                Map.entry(EXPIRED_RAW_TOKEN, EXPIRED_TOKEN));

        public static final Set<Token> TEST_TOKENS = Set.of(VALID_TOKEN, SECOND_VALID_TOKEN, EXPIRED_TOKEN);

        public static final Set<String> TOKEN_KEYS = Set.of(VALID_RAW_TOKEN, SECOND_VALID_RAW_TOKEN, EXPIRED_RAW_TOKEN);
    }

    public static final class TokenEntities {
        public static final TokenEntity TOKEN_ENTITY = TokenEntity.builder()
                .value(VALID_RAW_TOKEN)
                .expirationDate(TestConstants.TimeFixture.DATE_FAR_FUTURE)
                .build();
    }

    public static final class Users {
        public static final String VALID_USERNAME = "jane.doe@example.com";
        public static final String SECOND_VALID_USERNAME = "john.doe@example.com";
        public static final String EXPIRED_USERNAME = "old.user@example.com";
        public static final String USER_WITH_DIFFERENT_TOKEN_USERNAME = "USER_WITH_DIFFERENT_TOKEN";

        public static final User VALID_USER = User.of(VALID_USERNAME, VALID_TOKEN);
        public static final User SECOND_VALID_USER = User.of(SECOND_VALID_USERNAME, SECOND_VALID_TOKEN);
        public static final User EXPIRED_USER = User.of(EXPIRED_USERNAME, EXPIRED_TOKEN);

        public static final Map<String, User> USERS_DICTIONARY = Map.ofEntries(Map.entry(VALID_USERNAME, VALID_USER),
                Map.entry(SECOND_VALID_USERNAME, SECOND_VALID_USER),
                Map.entry(EXPIRED_USERNAME, EXPIRED_USER));

        public static final Set<User> TEST_USERS = Set.of(VALID_USER, SECOND_VALID_USER, EXPIRED_USER);

        public static final Set<String> USER_KEYS = Set.of(VALID_USERNAME, SECOND_VALID_USERNAME, EXPIRED_USERNAME);
    }

    public static final class UserEntities {

        public static final UserEntity USER_ENTITY = UserEntity.builder()
                .username(Users.VALID_USERNAME)
                .token(TestConstants.TokenEntities.TOKEN_ENTITY)
                .build();
    }
}