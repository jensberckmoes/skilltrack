package com.sopra_steria.jens_berckmoes.integration;

import com.sopra_steria.jens_berckmoes.TestConstants;
import com.sopra_steria.jens_berckmoes.domain.LoginResult;
import com.sopra_steria.jens_berckmoes.domain.LoginStatus;
import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.domain.repository.UserRepository;
import com.sopra_steria.jens_berckmoes.domain.valueobject.TokenValue;
import com.sopra_steria.jens_berckmoes.domain.valueobject.Username;
import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;
import com.sopra_steria.jens_berckmoes.infra.mapping.UserMapper;
import com.sopra_steria.jens_berckmoes.service.LoginService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static com.sopra_steria.jens_berckmoes.TestConstants.TimeFixture.DATE_FAR_FUTURE;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.TEST_USERS;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.VALID_USERNAME;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@ActiveProfiles("prod")
@DisplayName("LoginService using a real database")
class LoginServiceIntegrationTest {
    @Autowired private LoginService loginService;
    @Autowired private UserRepository userRepository;
    @Autowired private TokenRepository tokenRepository;

    @AfterEach
    void teardown() {
        wipeDatabaseClean();
    }

    @BeforeEach
    void setup() {
        wipeDatabaseClean();
        setUpTestUsersAndTokens();
    }

    private void setUpTestUsersAndTokens() {
        final Set<UserEntity> userEntities = UserMapper.mapToInfra(TEST_USERS);
        userRepository.saveAll(userEntities);
    }

    @Test
    @DisplayName("Should successfully login")
    void shouldLoginWithRealDatabase() {
        final LoginResult result = loginService.login(Username.of(VALID_USERNAME),
                TokenValue.of(TestConstants.Tokens.VALID_RAW_TOKEN));

        assertThat(result.loginStatus()).isEqualTo(LoginStatus.SUCCESS);
    }

    @Test
    @DisplayName("Should block login when username is invalid")
    void shouldBlockWhenUsernameIsInvalid() {
        final User user = userWithDefaultToken("-");

        final LoginResult result = loginService.login(Username.of(user.username()),
                TokenValue.of(defaultTestToken().token()));

        assertThat(result.loginStatus()).isEqualTo(LoginStatus.BLOCKED);
    }

    private Token defaultTestToken() {
        return Token.of("ABCD", DATE_FAR_FUTURE);
    }

    private User userWithDefaultToken(final String username) {
        return User.of(username, defaultTestToken());
    }

    private void wipeDatabaseClean() {
        userRepository.deleteAll();
        tokenRepository.deleteAll();
    }

}
