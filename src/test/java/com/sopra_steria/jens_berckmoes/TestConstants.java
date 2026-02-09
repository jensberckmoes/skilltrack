package com.sopra_steria.jens_berckmoes;

import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.dto.UserDto;
import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static com.sopra_steria.jens_berckmoes.TestConstants.TimeFixture.*;
import static com.sopra_steria.jens_berckmoes.TestConstants.TokenEntities.VALID_TOKEN_ENTITY_FOR_ONE_MORE_DAY;
import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.*;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.VALID_USERNAME_FOR_ONE_MORE_DAY_RAW_STRING;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.VALID_USERNAME_FOR_TEN_YEARS_RAW_STRING;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestConstants {

    public static final String BLANK = " ";

    public static final class TimeFixture {
        public static final LocalDate TEST_TODAY = LocalDate.of(2026, 1, 30);

        public static final LocalDate TEST_TEN_YEARS_FROM_NOW = TEST_TODAY.plusYears(10);
        public static final LocalDate TEST_TOMORROW = TEST_TODAY.plusDays(1);
        public static final LocalDate TEST_YESTERDAY = TEST_TODAY.minusDays(1);
    }

    public static final class Tokens {
        public static final String VALID_TOKEN_FOR_TEN_YEARS_RAW_STRING = "valid_token_for_ten_years";
        public static final String VALID_TOKEN_FOR_ONE_MORE_DAY_RAW_STRING = "valid_token_for_one_more_day";
        public static final String EXPIRED_TOKEN_BY_ONE_DAY_RAW_STRING = "expired_token_by_one_day";

        public static final Token VALID_TOKEN_FOR_TEN_YEARS = Token.of(VALID_TOKEN_FOR_TEN_YEARS_RAW_STRING,
                TEST_TEN_YEARS_FROM_NOW);
        public static final Token VALID_TOKEN_FOR_ONE_MORE_DAY = Token.of(VALID_TOKEN_FOR_ONE_MORE_DAY_RAW_STRING,
                TEST_TOMORROW);
        public static final Token EXPIRED_TOKEN_BY_ONE_DAY = Token.of(EXPIRED_TOKEN_BY_ONE_DAY_RAW_STRING,
                TEST_YESTERDAY);

        public static final Map<String, Token> BDD_TOKENS = Map.ofEntries(
                Map.entry("valid_token", Token.of("valid_token", TEST_TEN_YEARS_FROM_NOW)),
                Map.entry("expired_token", Token.of("expired_token", TEST_YESTERDAY)),
                Map.entry("other_valid_token", Token.of("other_valid_token", TEST_TOMORROW)),
                Map.entry("wrong_token", Token.NULL));

        public static final Set<Token> TOKENS_AS_SET = Set.of(VALID_TOKEN_FOR_TEN_YEARS,
                VALID_TOKEN_FOR_ONE_MORE_DAY,
                EXPIRED_TOKEN_BY_ONE_DAY);

        public static final Set<String> TOKEN_VALUES_AS_SET = Set.of(VALID_TOKEN_FOR_TEN_YEARS_RAW_STRING,
                VALID_TOKEN_FOR_ONE_MORE_DAY_RAW_STRING,
                EXPIRED_TOKEN_BY_ONE_DAY_RAW_STRING);
    }

    public static final class TokenEntities {
        public static final TokenEntity VALID_TOKEN_ENTITY_FOR_TEN_YEARS = TokenEntity.builder()
                .value(VALID_TOKEN_FOR_TEN_YEARS_RAW_STRING)
                .expirationDate(TEST_TEN_YEARS_FROM_NOW)
                .build();

        public static final TokenEntity VALID_TOKEN_ENTITY_FOR_ONE_MORE_DAY = TokenEntity.builder()
                .value(VALID_TOKEN_FOR_ONE_MORE_DAY_RAW_STRING)
                .expirationDate(TEST_TOMORROW)
                .build();
    }

    public static final class Users {
        public static final String VALID_USERNAME_FOR_TEN_YEARS_RAW_STRING = "valid_username_for_ten_years";
        public static final String VALID_USERNAME_FOR_ONE_MORE_DAY_RAW_STRING = "valid_username_for_one_more_day";
        public static final String EXPIRED_USERNAME_BY_ONE_DAY_RAW_STRING = "expired_username_by_one_day";
        public static final String USER_WITH_DIFFERENT_TOKEN_USERNAME = "user_with_different_token";

        public static final User VALID_USER_FOR_TEN_YEAR = User.of(VALID_USERNAME_FOR_TEN_YEARS_RAW_STRING, VALID_TOKEN_FOR_TEN_YEARS);
        public static final User VALID_USER_FOR_ONE_MORE_DAY = User.of(VALID_USERNAME_FOR_ONE_MORE_DAY_RAW_STRING,
                VALID_TOKEN_FOR_ONE_MORE_DAY);
        public static final User EXPIRED_USER_BY_ONE_DAY = User.of(EXPIRED_USERNAME_BY_ONE_DAY_RAW_STRING,
                EXPIRED_TOKEN_BY_ONE_DAY);

        public static final Map<String, User> BDD_USERS = Map.ofEntries(
                Map.entry("jane.doe@example.com", User.of("jane.doe@example.com", Token.of("valid_token", TEST_TEN_YEARS_FROM_NOW))),
                Map.entry("old.user@example.com", User.of("old.user@example.com", Token.of("expired_token", TEST_YESTERDAY))),
                Map.entry("belongs_to_other_user@example.com",User.of("belongs_to_other_user@example.com", Token.of("valid_token", TEST_TEN_YEARS_FROM_NOW))),
                Map.entry("nonexistent_user", User.of("nonexistent_user", Token.NULL)));

        public static final Set<User> USERS_AS_SET = Set.of(VALID_USER_FOR_TEN_YEAR,
                VALID_USER_FOR_ONE_MORE_DAY, EXPIRED_USER_BY_ONE_DAY);

        public static final Set<String> USERNAMES_AS_SET = Set.of(VALID_USERNAME_FOR_TEN_YEARS_RAW_STRING,
                VALID_USERNAME_FOR_ONE_MORE_DAY_RAW_STRING,
                EXPIRED_USERNAME_BY_ONE_DAY_RAW_STRING);
    }

    public static final class UserEntities {

        public static final UserEntity VALID_USER_ENTITY_FOR_TEN_YEARS = UserEntity.builder()
                .username(VALID_USERNAME_FOR_TEN_YEARS_RAW_STRING)
                .token(TokenEntities.VALID_TOKEN_ENTITY_FOR_TEN_YEARS)
                .build();

        public static final UserEntity VALID_USER_ENTITY_FOR_ONE_MORE_DAY = UserEntity.builder()
                .username(VALID_USERNAME_FOR_ONE_MORE_DAY_RAW_STRING)
                .token(VALID_TOKEN_ENTITY_FOR_ONE_MORE_DAY)
                .build();
    }

    public static final class UserDtos {

        public static final UserDto VALID_USER_DTO_FOR_TEN_YEARS = UserDto.of(VALID_USERNAME_FOR_TEN_YEARS_RAW_STRING);

        public static final UserDto VALID_USER_DTO_FOR_ONE_MORE_DAY = UserDto.of(VALID_USERNAME_FOR_ONE_MORE_DAY_RAW_STRING);
    }
}