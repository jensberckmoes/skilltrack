package com.sopra_steria.jens_berckmoes;

import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.dto.GetUserResponse;
import com.sopra_steria.jens_berckmoes.domain.valueobject.TokenValue;
import com.sopra_steria.jens_berckmoes.domain.valueobject.Username;
import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sopra_steria.jens_berckmoes.TestConstants.TimeFixture.TEST_TODAY;
import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.*;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestConstants {

    public static final String BLANK = " ";

    public static final class TestMethods {
        public static void flushAndResetContext(final Runnable action, final EntityManager entityManager) {
            action.run();
            entityManager.flush();
            entityManager.clear();
        }
    }


    public static final class TimeFixture {
        public static final LocalDate TEST_TODAY = LocalDate.of(2026, 1, 30);
        public static final Clock FIXED_CLOCK = Clock.fixed(TEST_TODAY.atStartOfDay().atZone(ZoneOffset.UTC).toInstant(), ZoneOffset.UTC);
    }

    public static final class Tokens {
        public static final String NON_EXISTING_TOKEN_RAW_STRING = "abc123";
        public static final String ALICE_TOKEN_RAW_STRING = "A1b2C3d4E5f6G7h8I9j";

        public static final Token ALICE_TOKEN = Token.of(ALICE_TOKEN_RAW_STRING, TEST_TODAY);
        public static final Token BOB_TOKEN = Token.of("Z9y8X7w6V5u4T3s2R1q", TEST_TODAY.minusDays(1));
        public static final Token CHARLIE_TOKEN = Token.of("M1n2B3v4C5x6Z7l8K9p", TEST_TODAY.minusDays(8));
        public static final Token DAVE_TOKEN = Token.of("Q1w2E3r4T5y6U7i8O9p", TEST_TODAY);
        public static final Token GEERT_TOKEN = Token.of("X1y2Z3a4B5c6D7e8F9g", TEST_TODAY.plusDays(7));

        public static final Map<String, Token> BDD_TOKENS_WITH_REALISTIC_VALUES = Map.ofEntries(
                Map.entry(ALICE_TOKEN.token(), ALICE_TOKEN),
                Map.entry(BOB_TOKEN.token(), BOB_TOKEN),
                Map.entry(CHARLIE_TOKEN.token(), CHARLIE_TOKEN),
                Map.entry(DAVE_TOKEN.token(), DAVE_TOKEN),
                Map.entry(GEERT_TOKEN.token(), GEERT_TOKEN)
        );

    }

    public static final class TokenValues {
        public static final TokenValue ALICE_TOKEN_VALUE = TokenValue.of(ALICE_TOKEN.token());
        public static final TokenValue BOB_TOKEN_VALUE = TokenValue.of(BOB_TOKEN.token());
    }

    public static final class TokenEntities {
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

        public static final Set<TokenEntity> TOKEN_ENTITIES_AS_SET = BDD_TOKENS_WITH_REALISTIC_VALUES.values().stream()
                .map(token -> TokenEntity.builder()
                        .value(token.token())
                        .expirationDate(token.expirationDate())
                        .build())
                .collect(Collectors.toSet());

    }

    public static final class Users {
        public static final String NON_EXISTING_USERNAME_RAW_STRING = "xq7";
        public static final String ALICE_USERNAME_RAW_STRING = "alice@example.com";

        public static final User ALICE = User.of("alice@example.com", ALICE_TOKEN);
        public static final User BOB = User.of("bob@example.com", BOB_TOKEN);
        public static final User CHARLIE = User.of("charlie@example.com", CHARLIE_TOKEN);
        public static final User DAVE = User.of("dave@example.com", DAVE_TOKEN);
        public static final User GEERT = User.of("geert@example.com", GEERT_TOKEN);

        public static final User ALICE_USER_DTO = User.nullToken(ALICE.username());
        public static final User BOB_USER_DTO = User.nullToken(BOB.username());

        public static final Map<String, User> BDD_USERS_WITH_REALISTIC_VALUES = Map.ofEntries(
                Map.entry(ALICE.username(), ALICE),
                Map.entry(BOB.username(), BOB),
                Map.entry(CHARLIE.username(), CHARLIE),
                Map.entry(DAVE.username(), DAVE),
                Map.entry(GEERT.username(), GEERT)
        );

        public static final Set<User> USERS_AS_SET = Set.copyOf(BDD_USERS_WITH_REALISTIC_VALUES.values());
    }

    public static final class Usernames {
        public static final Username NON_EXISTING_USERNAME = Username.of("xq7");
        public static final Username ALICE_USERNAME = Username.of(ALICE.username());
    }

    public static final class UserEntities {

        public static final UserEntity ALICE_ENTITY = UserEntity
                .builder()
                .username(ALICE.username())
                .token(TokenEntities.ALICE_TOKEN_ENTITY)
                .build();

        public static final UserEntity BOB_ENTITY = UserEntity
                .builder()
                .username(BOB.username())
                .token(TokenEntities.BOB_TOKEN_ENTITY)
                .build();

        public static final UserEntity CHARLIE_ENTITY = UserEntity
                .builder()
                .username(CHARLIE.username())
                .token(TokenEntities.CHARLIE_TOKEN_ENTITY)
                .build();

        public static final Set<UserEntity> USER_ENTITIES_AS_SET = USERS_AS_SET.stream()
                .map(user -> UserEntity.builder()
                        .username(user.username())
                        .token(TokenEntity.builder().value(user.token().token()).expirationDate(user.token().expirationDate()).build())
                        .build())
                .collect(Collectors.toSet());
    }

    public static final class GetUserResponses {

        public static final Set<GetUserResponse> GET_USER_RESPONSES_AS_SET = USERS_AS_SET.stream()
                .map(user -> GetUserResponse.of(user.username()))
                .collect(Collectors.toSet());

        public static final GetUserResponse ALICE_DTO = GetUserResponse.of(ALICE.username());
        public static final GetUserResponse BOB_DTO = GetUserResponse.of(BOB.username());

    }
}