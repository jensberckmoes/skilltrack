package com.sopra_steria.jens_berckmoes.service;

import com.sopra_steria.jens_berckmoes.model.LoginResult;
import com.sopra_steria.jens_berckmoes.model.LoginStatus;
import com.sopra_steria.jens_berckmoes.model.Token;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LoginServiceTest {

    private final LoginService loginService = new LoginService();

    @Test
    void shouldBlockWhenUsernameIsInvalid() {
        final Token validToken = Token.of("valid_token");

        final LoginResult result = loginService.login("nonexistent_user", validToken);

        assertThat(result)
                .isEqualTo(LoginResult.of(LoginStatus.Blocked, "please contact support"));
    }

    @Test
    void shouldBlockWhenTokenIsInvalid() {
        final Token invalidToken = Token.of("wrong_token");

        final LoginResult result = loginService.login("jane.doe@example.com", invalidToken);

        assertThat(result)
                .isEqualTo(LoginResult.of(LoginStatus.Blocked, "please contact support"));
    }

}