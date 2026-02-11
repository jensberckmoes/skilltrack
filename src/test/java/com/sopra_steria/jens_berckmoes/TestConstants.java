package com.sopra_steria.jens_berckmoes;

import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.dto.GetUserResponse;
import com.sopra_steria.jens_berckmoes.domain.valueobject.TokenValue;
import com.sopra_steria.jens_berckmoes.domain.valueobject.Username;
import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sopra_steria.jens_berckmoes.TestConstants.TimeFixture.*;
import static com.sopra_steria.jens_berckmoes.TestConstants.TokenEntities.VALID_TOKEN_ENTITY_FOR_ONE_MORE_DAY;
import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.*;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.*;

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
        public static final String NON_EXISTING_TOKEN_RAW_STRING = "abc123";

        public static final Token VALID_TOKEN_FOR_TEN_YEARS = Token.of(VALID_TOKEN_FOR_TEN_YEARS_RAW_STRING, TEST_TEN_YEARS_FROM_NOW);
        public static final Token VALID_TOKEN_FOR_ONE_MORE_DAY = Token.of(VALID_TOKEN_FOR_ONE_MORE_DAY_RAW_STRING, TEST_TOMORROW);

        public static final Token ALICE_TOKEN = Token.of("A1b2C3d4E5f6G7h8I9j", TEST_TODAY);
        public static final Token BOB_TOKEN = Token.of("Z9y8X7w6V5u4T3s2R1q", TEST_YESTERDAY);
        public static final Token CHARLIE_TOKEN = Token.of("M1n2B3v4C5x6Z7l8K9p", TEST_TODAY.minusDays(8));
        public static final Token DAVE_TOKEN = Token.of("Q1w2E3r4T5y6U7i8O9p", TEST_TODAY);
        public static final Token EVE_TOKEN = Token.of("L9k8J7h6G5f4D3s2A1z", TEST_TODAY.minusDays(10));
        public static final Token FRANK_TOKEN = Token.of("B2c3D4e5F6g7H8i9J0k", TEST_TODAY.plusDays(3));
        public static final Token GEERT_TOKEN = Token.of("X1y2Z3a4B5c6D7e8F9g", TEST_TODAY.plusDays(7));

        public static Map<String, Token> BDD_TOKENS_WITH_REALISTIC_VALUES = createRealisticTokens();

        private static Map<String, Token> createRealisticTokens() {
            final Map<String, Token> tokens = new HashMap<>();
            tokens.put(ALICE_TOKEN.token(), ALICE_TOKEN);
            tokens.put(BOB_TOKEN.token(), BOB_TOKEN);
            tokens.put(CHARLIE_TOKEN.token(), CHARLIE_TOKEN);
            tokens.put(DAVE_TOKEN.token(), DAVE_TOKEN);
            tokens.put(EVE_TOKEN.token(), EVE_TOKEN);
            tokens.put(FRANK_TOKEN.token(), FRANK_TOKEN);
            tokens.put(GEERT_TOKEN.token(), GEERT_TOKEN);
            return tokens;
        }

        public static final Set<Token> TOKENS_AS_SET = new HashSet<>(createRealisticTokens().values());
        public static final Set<TokenValue> TOKEN_VALUE_SET = TOKENS_AS_SET.stream()
                .map(token -> TokenValue.of(token.token()))
                .collect(Collectors.toSet());
        public static final Set<String> TOKEN_VALUES_AS_SET = TOKEN_VALUE_SET.stream().map(TokenValue::value).collect(Collectors.toSet());
    }

    public static final class TokenValues {
        public static final TokenValue VALID_TOKEN_VALUE_FOR_TEN_YEARS = TokenValue.of(VALID_TOKEN_FOR_TEN_YEARS_RAW_STRING);

        public static final TokenValue ALICE_TOKEN_VALUE = TokenValue.of(ALICE_TOKEN.token());
        public static final TokenValue BOB_TOKEN_VALUE = TokenValue.of(BOB_TOKEN.token());
    }

    public static final class TokenEntities {
        public static final TokenEntity VALID_TOKEN_ENTITY_FOR_TEN_YEARS = TokenEntity
                .builder()
                .value(VALID_TOKEN_FOR_TEN_YEARS_RAW_STRING)
                .expirationDate(TEST_TEN_YEARS_FROM_NOW).build();

        public static final TokenEntity VALID_TOKEN_ENTITY_FOR_ONE_MORE_DAY = TokenEntity
                .builder()
                .value(VALID_TOKEN_FOR_ONE_MORE_DAY_RAW_STRING)
                .expirationDate(TEST_TOMORROW)
                .build();

        public static final TokenEntity ALICE_TOKEN_ENTITY = TokenEntity
                .builder()
                .value(ALICE_TOKEN.token())
                .expirationDate(ALICE_TOKEN.expirationDate())
                .build();

        public static final TokenEntity BOB_TOKEN_ENTITY = TokenEntity
                .builder()
                .value(BOB_TOKEN.token())
                .expirationDate(BOB_TOKEN.expirationDate())
                .build();

        public static final TokenEntity CHARLIE_TOKEN_ENTITY = TokenEntity
                .builder()
                .value(CHARLIE_TOKEN.token())
                .expirationDate(CHARLIE_TOKEN.expirationDate())
                .build();

        public static Set<TokenEntity> TOKEN_ENTITIES_AS_SET = TOKENS_AS_SET.stream()
                .map(token -> TokenEntity.builder()
                        .value(token.token())
                        .expirationDate(token.expirationDate())
                        .build())
                .collect(Collectors.toSet());

    }

    public static final class Users {
        public static final String VALID_USERNAME_FOR_TEN_YEARS_RAW_STRING = "valid_username_for_ten_years";
        public static final String VALID_USERNAME_FOR_ONE_MORE_DAY_RAW_STRING = "valid_username_for_one_more_day";
        public static final String NON_EXISTING_USERNAME_RAW_STRING = "xq7";

        public static final User VALID_USER_FOR_TEN_YEAR = User.of(VALID_USERNAME_FOR_TEN_YEARS_RAW_STRING, VALID_TOKEN_FOR_TEN_YEARS);
        public static final User VALID_USER_FOR_ONE_MORE_DAY = User.of(VALID_USERNAME_FOR_ONE_MORE_DAY_RAW_STRING, VALID_TOKEN_FOR_ONE_MORE_DAY);

        public static final User ALICE = User.of("alice@example.com", ALICE_TOKEN);
        public static final User BOB = User.of("bob@example.com", BOB_TOKEN);
        public static final User CHARLIE = User.of("charlie@example.com", CHARLIE_TOKEN);
        public static final User DAVE = User.of("dave@example.com", DAVE_TOKEN);
        public static final User EVE = User.of("eve@example.com", EVE_TOKEN);
        public static final User FRANK = User.of("frank@example.com", FRANK_TOKEN);
        public static final User GEERT = User.of("geert@example.com", GEERT_TOKEN);

        public static Map<String, User> BDD_USERS_WITH_REALISTIC_VALUES = createRealisticUsers();

        private static Map<String, User> createRealisticUsers() {
            final Map<String, User> users = new HashMap<>();
            users.put(ALICE.username(), ALICE);
            users.put(BOB.username(), BOB);
            users.put(CHARLIE.username(), CHARLIE);
            users.put(DAVE.username(), DAVE);
            users.put(EVE.username(), EVE);
            users.put(FRANK.username(), FRANK);
            users.put(GEERT.username(), GEERT);
            return users;
        }

        public static final Set<User> USERS_AS_SET = new HashSet<>(createRealisticUsers().values());
        public static final Set<Username> USERNAME_SET = USERS_AS_SET.stream()
                .map(user -> Username.of(user.username()))
                .collect(Collectors.toSet());
        public static final Set<String> USERNAMES_AS_SET = USERNAME_SET.stream().map(Username::value).collect(Collectors.toSet());
    }

    public static final class Usernames {
        public static final Username VALID_USERNAME_FOR_TEN_YEARS = Username.of(VALID_USERNAME_FOR_TEN_YEARS_RAW_STRING);
        public static final Username VALID_USERNAME_FOR_ONE_MORE_DAY = Username.of(VALID_USERNAME_FOR_ONE_MORE_DAY_RAW_STRING);
        public static final Username NON_EXISTING_USERNAME = Username.of("xq7");
    }

    public static final class UserEntities {

        public static final UserEntity VALID_USER_ENTITY_FOR_TEN_YEARS = UserEntity
                .builder()
                .username(VALID_USERNAME_FOR_TEN_YEARS_RAW_STRING)
                .token(TokenEntities.VALID_TOKEN_ENTITY_FOR_TEN_YEARS)
                .build();

        public static final UserEntity VALID_USER_ENTITY_FOR_ONE_MORE_DAY = UserEntity
                .builder()
                .username(VALID_USERNAME_FOR_ONE_MORE_DAY_RAW_STRING)
                .token(VALID_TOKEN_ENTITY_FOR_ONE_MORE_DAY)
                .build();

        public static final UserEntity ALICE_ENTITY = UserEntity
                .builder()
                .username(ALICE.username())
                .token(TokenEntities.ALICE_TOKEN_ENTITY)
                .build();

        public static Set<UserEntity> USER_ENTITIES_AS_SET = USERS_AS_SET.stream()
                .map(user -> UserEntity.builder()
                        .username(user.username())
                        .token(TokenEntity.builder().value(user.token().token()).expirationDate(user.token().expirationDate()).build())
                        .build())
                .collect(Collectors.toSet());
    }

    public static final class getUserResponses {

        public static final Set<GetUserResponse> GET_USER_RESPONSES_AS_SET = USERS_AS_SET.stream()
                .map(user -> GetUserResponse.of(user.username()))
                .collect(Collectors.toSet());

        public static final GetUserResponse VALID_USER_DTO_FOR_TEN_YEARS = GetUserResponse.of(VALID_USERNAME_FOR_TEN_YEARS_RAW_STRING);

        public static final GetUserResponse VALID_USER_DTO_FOR_ONE_MORE_DAY = GetUserResponse.of(VALID_USERNAME_FOR_ONE_MORE_DAY_RAW_STRING);
    }
}