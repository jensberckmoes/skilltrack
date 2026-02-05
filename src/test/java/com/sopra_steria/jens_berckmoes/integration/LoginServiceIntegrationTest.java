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
import com.sopra_steria.jens_berckmoes.service.LoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@ActiveProfiles("prod")
class LoginServiceIntegrationTest {
    @Autowired private LoginService loginService;
    @Autowired private UserRepository userRepository;
    @Autowired private TokenRepository tokenRepository;

    @Test
    void shouldLoginWithRealDatabase() {
        final String tokenValue = "ABCD";
        final String username = "testUser";
        final Token token = Token.of(tokenValue, TestConstants.TimeFixture.REFERENCE_DATE);
        tokenRepository.save(token);
        userRepository.save(User.of(username, token));

        final LoginResult result = loginService.login(Username.of(username), TokenValue.of(tokenValue));

        assertThat(result.loginStatus()).isEqualTo(LoginStatus.SUCCESS);
    }
}