package com.sopra_steria.jens_berckmoes.integration;

import com.sopra_steria.jens_berckmoes.domain.LoginResult;
import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.domain.repository.UserRepository;
import com.sopra_steria.jens_berckmoes.domain.valueobject.TokenValue;
import com.sopra_steria.jens_berckmoes.domain.valueobject.Username;
import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;
import com.sopra_steria.jens_berckmoes.integration.config.IntegrationConfig;
import com.sopra_steria.jens_berckmoes.service.ValidateAssessmentAccessService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.*;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.*;
import static com.sopra_steria.jens_berckmoes.domain.LoginResult.blocked;
import static com.sopra_steria.jens_berckmoes.domain.LoginResult.success;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(IntegrationConfig.class)
@ActiveProfiles("integration")
@DisplayName("LoginService using a real database")
class ValidateAssessmentAccessServiceIntegrationTest {
    @Autowired
    private ValidateAssessmentAccessService validateAssessmentAccessService;
    @Autowired
    private Clock clock;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;

    @AfterEach
    void teardown() {
        wipeDatabaseClean();
    }

    @BeforeEach
    void setup() {
        wipeDatabaseClean();
        setUpTestUsersAndTokens();
    }

    @ParameterizedTest
    @MethodSource("reasonsToBlockLoginParameters")
    @DisplayName("should block login when username or token are invalid, when token has expired or when token does not match the one associated with the user")
    void shouldDecideLogin(final String username, final String tokenValue, final LoginResult expectedResult) {
        //Act
        final LoginResult result = validateAssessmentAccessService.login(Username.of(username), TokenValue.of(tokenValue));
        //Assert
        assertThat(result).isEqualTo(expectedResult);

    }

    public static Stream<Arguments> reasonsToBlockLoginParameters() {
        return Stream.of(Arguments.of(NON_EXISTING_USERNAME_RAW_STRING, NON_EXISTING_TOKEN_RAW_STRING, blocked()),
                Arguments.of(NON_EXISTING_USERNAME_RAW_STRING, ALICE_TOKEN.token(), blocked()),
                Arguments.of(GEERT.username(), ALICE_TOKEN.token(), blocked()),
                Arguments.of(BOB.username(), BOB_TOKEN.token(), blocked()),
                Arguments.of(ALICE.username(), NON_EXISTING_TOKEN_RAW_STRING, blocked()),
                Arguments.of(ALICE.username(), ALICE_TOKEN.token(), success()));
    }

    private void wipeDatabaseClean() {
        userRepository.deleteAll();
        tokenRepository.deleteAll();
    }

    private void setUpTestUsersAndTokens() {
        final LocalDate fixedNow = LocalDate.now(clock);
        final Token ALICE_TOKEN = Token.of("A1b2C3d4E5f6G7h8I9j", fixedNow);
        final Token BOB_TOKEN = Token.of("Z9y8X7w6V5u4T3s2R1q", fixedNow.minusDays(1));
        final Token CHARLIE_TOKEN = Token.of("M1n2B3v4C5x6Z7l8K9p", fixedNow.minusDays(8));
        final Token DAVE_TOKEN = Token.of("Q1w2E3r4T5y6U7i8O9p", fixedNow);
        final Token EVE_TOKEN = Token.of("L9k8J7h6G5f4D3s2A1z", fixedNow.minusDays(10));
        final Token FRANK_TOKEN = Token.of("B2c3D4e5F6g7H8i9J0k", fixedNow.plusDays(3));
        final Token GEERT_TOKEN = Token.of("X1y2Z3a4B5c6D7e8F9g", fixedNow.plusDays(7));

        final User ALICE = User.of("alice@example.com", ALICE_TOKEN);
        final User BOB = User.of("bob@example.com", BOB_TOKEN);
        final User CHARLIE = User.of("charlie@example.com", CHARLIE_TOKEN);
        final User DAVE = User.of("dave@example.com", DAVE_TOKEN);
        final User EVE = User.of("eve@example.com", EVE_TOKEN);
        final User FRANK = User.of("frank@example.com", FRANK_TOKEN);
        final User GEERT = User.of("geert@example.com", GEERT_TOKEN);

        final Set<User> USERS_AS_SET = Set.of(ALICE, BOB, CHARLIE, DAVE, EVE, FRANK, GEERT);

        final Set<UserEntity> userEntities = USERS_AS_SET.stream()
                .map(user -> UserEntity.builder()
                        .username(user.username())
                        .token(TokenEntity.builder().value(user.token().token()).expirationDate(user.token().expirationDate()).build())
                        .build())
                .collect(Collectors.toSet());

        userRepository.saveAll(userEntities);
    }

}
