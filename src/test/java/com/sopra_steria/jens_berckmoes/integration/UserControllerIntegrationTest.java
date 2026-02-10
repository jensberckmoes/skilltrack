package com.sopra_steria.jens_berckmoes.integration;

import com.sopra_steria.jens_berckmoes.domain.dto.UserDto;
import com.sopra_steria.jens_berckmoes.domain.dto.UserDtoResponse;
import com.sopra_steria.jens_berckmoes.domain.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.domain.repository.UserRepository;
import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Set;

import static com.sopra_steria.jens_berckmoes.TestConstants.Users.USERS_AS_SET;
import static com.sopra_steria.jens_berckmoes.domain.mapping.UserDtoMapper.toDtos;
import static com.sopra_steria.jens_berckmoes.infra.mapping.UserMapper.mapToInfra;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("prod")
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
        final UserDtoResponse response = webClient.get()
                .uri("/api/users")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<UserDtoResponse>() {})
                .returnResult()
                .getResponseBody();
        assertThat(response).isNotNull();
        assertThat(response.userDtos()).isNotEmpty();
        assertThat(response.userDtos()).containsExactlyInAnyOrder(toDtos(USERS_AS_SET).toArray(new UserDto[0]));
    }

    @Test
    @DisplayName("should get status code 204 No Content when there are no users found")
    void shouldGetNoContentWhenNoUsers() {
        wipeDatabaseClean();

        webClient.get().uri("/api/users").exchange().expectStatus().isNoContent().expectBody().isEmpty();
    }
}

