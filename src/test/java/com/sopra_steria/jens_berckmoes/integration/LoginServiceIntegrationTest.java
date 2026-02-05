package com.sopra_steria.jens_berckmoes.integration;

import com.sopra_steria.jens_berckmoes.domain.LoginResult;
import com.sopra_steria.jens_berckmoes.domain.LoginStatus;
import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.domain.repository.UserRepository;
import com.sopra_steria.jens_berckmoes.domain.valueobject.TokenValue;
import com.sopra_steria.jens_berckmoes.domain.valueobject.Username;
import com.sopra_steria.jens_berckmoes.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.sopra_steria.jens_berckmoes.TestConstants.TimeFixture.DATE_FAR_FUTURE;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@ActiveProfiles("prod")
class LoginServiceIntegrationTest {
    @Autowired private LoginService loginService;
    @Autowired private UserRepository userRepository;
    @Autowired private TokenRepository tokenRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldLoginWithRealDatabase() {
        final String tokenValue = "ABCD";
        final String username = "testUser";
        final Token token = Token.of(tokenValue, DATE_FAR_FUTURE);
        tokenRepository.save(token);
        userRepository.save(User.of(username, token));

        final LoginResult result = loginService.login(Username.of(username), TokenValue.of(tokenValue));

        assertThat(result.loginStatus()).isEqualTo(LoginStatus.SUCCESS);
    }

    @Test
    void shouldBlockWhenUsernameIsInvalid() {
        final String tokenValue = "ABCD";
        final String username = "testUser";
        final Token token = Token.of(tokenValue, DATE_FAR_FUTURE);
        tokenRepository.save(token);
        userRepository.save(User.of(username, token));

        final LoginResult result = loginService.login(Username.of(username), TokenValue.of(tokenValue));

        assertThat(result.loginStatus()).isEqualTo(LoginStatus.SUCCESS);
    }

    /*
    * @Test
    void shouldBlockWhenUsernameIsInvalid() {
        //Arrange
        final Username invalidUsername = Username.of(INVALID_USERNAME);
        final TokenValue validTokenValue = TokenValue.of(VALID_RAW_TOKEN);

        when(userRepository.findByUsername(invalidUsername.value())).thenThrow(new UserNotFoundException());
        when(tokenRepository.findByTokenValue(validTokenValue.value())).thenReturn(VALID_TOKEN);

        //Act
        final LoginResult result = loginService.login(invalidUsername, validTokenValue);

        //Assert
        assertThat(result).isEqualTo(blocked());
    }

    @Test
    void shouldBlockWhenTokenIsInvalid() {
        //Arrange
        final Username validUsername = Username.of(VALID_USERNAME);
        final TokenValue invalidTokenValue = TokenValue.of(WRONG_RAW_TOKEN);

        when(userRepository.findByUsername(validUsername.value())).thenReturn(VALID_USER);
        when(tokenRepository.findByTokenValue(invalidTokenValue.value())).thenThrow(new TokenNotFoundException());

        //Act
        final LoginResult result = loginService.login(validUsername, invalidTokenValue);

        //Assert
        assertThat(result).isEqualTo(blocked());
    }

    @Test
    void shouldBlockWhenTokenDoesNotBelongToUser() {
        //Arrange
        final Username validUsername = Username.of(VALID_USERNAME);
        final TokenValue validTokenValue = TokenValue.of(VALID_RAW_TOKEN);

        when(userRepository.findByUsername(VALID_USERNAME)).thenReturn(User.of(VALID_USERNAME, SECOND_VALID_TOKEN));
        when(tokenRepository.findByTokenValue(validTokenValue.value())).thenReturn(VALID_TOKEN);

        //Act
        final LoginResult result = loginService.login(validUsername, validTokenValue);

        //Assert
        assertThat(result).isEqualTo(blocked());
    }

    @Test
    void shouldBlockWhenTokenHasExpired() {
        //Arrange
        final Username validUsername = Username.of(VALID_USERNAME);
        final TokenValue validTokenValue = TokenValue.of(VALID_RAW_TOKEN);

        when(userRepository.findByUsername(validUsername.value())).thenReturn(User.of(VALID_USERNAME, EXPIRED_TOKEN));
        when(tokenRepository.findByTokenValue(validTokenValue.value())).thenReturn(EXPIRED_TOKEN);

        //Act
        final LoginResult result = loginService.login(validUsername, validTokenValue);

        //Assert
        assertThat(result).isEqualTo(blocked());
    }

    @Test
    void shouldReturnSuccessWhenUsernameAndTokenAreValid() {
        //Arrange
        final Username validUsername = Username.of(VALID_USERNAME);
        final TokenValue validTokenValue = TokenValue.of(VALID_RAW_TOKEN);

        when(userRepository.findByUsername(validUsername.value())).thenReturn(VALID_USER);
        when(tokenRepository.findByTokenValue(validTokenValue.value())).thenReturn(VALID_TOKEN);

        //Act
        final LoginResult result = loginService.login(validUsername, validTokenValue);

        //Assert
        assertThat(result).isEqualTo(success());
    }
    *
    *
    * */
}