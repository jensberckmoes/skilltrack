package com.sopra_steria.jens_berckmoes.integration;

import com.sopra_steria.jens_berckmoes.TestConstants;
import com.sopra_steria.jens_berckmoes.domain.ErrorResponse;
import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.dto.GetAllUsersResponse;
import com.sopra_steria.jens_berckmoes.domain.dto.GetUserResponse;
import com.sopra_steria.jens_berckmoes.domain.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.domain.exception.UsernameRawValueNullOrBlankException;
import com.sopra_steria.jens_berckmoes.domain.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.domain.repository.UserRepository;
import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;
import com.sopra_steria.jens_berckmoes.integration.config.IntegrationConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Set;
import java.util.function.Consumer;

import static com.sopra_steria.jens_berckmoes.TestConstants.Users.USERS_AS_SET;
import static com.sopra_steria.jens_berckmoes.domain.mapping.UserDtoMapper.toGetUserResponse;
import static com.sopra_steria.jens_berckmoes.domain.mapping.UserDtoMapper.toGetUsersResponse;
import static com.sopra_steria.jens_berckmoes.infra.mapping.UserMapper.mapToInfra;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(IntegrationConfig.class)
@ActiveProfiles("integration")
@AutoConfigureWebTestClient
@DisplayName("User controller using a real database")
public class UserControllerIntegrationTest {
    @Autowired private WebTestClient webClient;
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

    private void wipeDatabaseClean() {
        userRepository.deleteAll();
        tokenRepository.deleteAll();
    }

    private void setUpTestUsersAndTokens() {
        final Set<UserEntity> userEntities = mapToInfra(USERS_AS_SET);
        userRepository.saveAll(userEntities);
    }

    @Test
    @DisplayName("should get all users as user dto objects with status code 200 OK")
    void shouldGetAllUsers() {
        final GetAllUsersResponse response = webClient.get()
                .uri("/api/users")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<GetAllUsersResponse>() {})
                .returnResult()
                .getResponseBody();
        assertThat(response).isNotNull();
        assertThat(response.getUserResponses()).isNotEmpty();
        assertThat(response.getUserResponses()).containsExactlyInAnyOrder(toGetUsersResponse(USERS_AS_SET).toArray(new GetUserResponse[0]));
    }

    @Test
    @DisplayName("should get status code 204 No Content when there are no users found")
    void shouldGetNoContentWhenNoUsers() {
        wipeDatabaseClean();

        webClient.get().uri("/api/users").exchange().expectStatus().isNoContent().expectBody().isEmpty();
    }

    @Test
    @DisplayName("should be able to get a User using username with status code 200 OK")
    void shouldBeAbleToGetUserByUsername() {
        wipeDatabaseClean();
        final User user = User.of("jane.doe@example.com", Token.of("abcdef", TestConstants.TimeFixture.TEST_TODAY));
        userRepository.save(user);

        webClient.get()
                .uri("/api/users/jane.doe@example.com")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(GetUserResponse.class)
                .isEqualTo(toGetUserResponse(user));
    }

    @Test
    @DisplayName("should get a 400 Bad Request status code when trying to get a user by null username")
    void shouldGetBadRequestWhenEmptyUsername() {
        webClient.get()
                .uri("/api/users/ ")
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(ErrorResponse.class)
                .value(t -> testErrorResponseUsernameWithMessage("Username can not be blank").accept(t));
    }

    @Test
    @DisplayName("should get a 400 Bad Request status code when trying to get a user by empty username")
    void shouldGetBadRequestWhenNoUsername() {
        webClient.get()
                .uri("/api/users/")
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(ErrorResponse.class)
                .value(t -> testErrorResponseUsernameWithMessage("Username can not be null").accept(t));
    }

    @Test
    @DisplayName("should get a 404 Not found status code when trying to get a non existing user")
    void shouldGetNotFoundWhenNonExistingUsername() {
        webClient.get()
                .uri("/api/users/xq7")
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(ErrorResponse.class)
                .value(t -> testErrorResponseWithMessage(new ErrorResponse("User not found: xq7",
                        404,
                        UserNotFoundException.class.getSimpleName())).accept(t));
    }

    private Consumer<ErrorResponse> testErrorResponseWithMessage(final ErrorResponse error) {
        return errorResponse -> {
            assertThat(errorResponse).isNotNull();
            assertThat(errorResponse.status()).isEqualTo(error.status());
            assertThat(errorResponse.exception()).isEqualTo(error.exception());
            assertThat(errorResponse.message()).isEqualTo(error.message());
        };
    }

    private Consumer<ErrorResponse> testErrorResponseUsernameWithMessage(final String message) {
        return testErrorResponseWithMessage(new ErrorResponse(message, 400, UsernameRawValueNullOrBlankException.class.getSimpleName()));
    }
}

