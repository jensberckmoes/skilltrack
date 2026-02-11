package com.sopra_steria.jens_berckmoes.integration;

import com.sopra_steria.jens_berckmoes.domain.LoginResult;
import com.sopra_steria.jens_berckmoes.domain.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.domain.repository.UserRepository;
import com.sopra_steria.jens_berckmoes.domain.valueobject.TokenValue;
import com.sopra_steria.jens_berckmoes.domain.valueobject.Username;
import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;
import com.sopra_steria.jens_berckmoes.infra.mapping.UserMapper;
import com.sopra_steria.jens_berckmoes.service.ValidateAssessmentAccessService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;
import java.util.stream.Stream;

import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.*;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.*;
import static com.sopra_steria.jens_berckmoes.domain.LoginResult.blocked;
import static com.sopra_steria.jens_berckmoes.domain.LoginResult.success;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("prod")
@DisplayName("LoginService using a real database")
class ValidateAssessmentAccessServiceIntegrationTest {
    @Autowired
    private ValidateAssessmentAccessService validateAssessmentAccessService;
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
                Arguments.of(NON_EXISTING_TOKEN_RAW_STRING, VALID_TOKEN_FOR_TEN_YEARS_RAW_STRING, blocked()),
                Arguments.of(USER_WITH_DIFFERENT_TOKEN_USERNAME_RAW_STRING, VALID_TOKEN_FOR_TEN_YEARS_RAW_STRING, blocked()),
                Arguments.of(EXPIRED_USERNAME_BY_ONE_DAY_RAW_STRING, EXPIRED_TOKEN_BY_ONE_DAY_RAW_STRING, blocked()),
                Arguments.of(VALID_USERNAME_FOR_TEN_YEARS_RAW_STRING, NON_EXISTING_TOKEN_RAW_STRING, blocked()),
                Arguments.of(VALID_USERNAME_FOR_TEN_YEARS_RAW_STRING, VALID_TOKEN_FOR_TEN_YEARS_RAW_STRING, success()));
    }

    private void wipeDatabaseClean() {
        userRepository.deleteAll();
        tokenRepository.deleteAll();
    }

    private void setUpTestUsersAndTokens() {
        final Set<UserEntity> userEntities = UserMapper.mapToInfra(USERS_AS_SET);
        userRepository.saveAll(userEntities);
    }

}
