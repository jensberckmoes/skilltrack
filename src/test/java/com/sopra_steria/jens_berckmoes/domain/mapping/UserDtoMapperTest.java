package com.sopra_steria.jens_berckmoes.domain.mapping;

import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.dto.GetUserResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.sopra_steria.jens_berckmoes.TestConstants.Users.*;
import static com.sopra_steria.jens_berckmoes.TestConstants.GetUserResponses.*;
import static com.sopra_steria.jens_berckmoes.domain.mapping.UserDtoMapper.*;
import static com.sopra_steria.jens_berckmoes.domain.mapping.UserDtoMapper.toGetUserResponse;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserDtoMapper")
public class UserDtoMapperTest {
    @Test
    @DisplayName("should correctly map from User to UserDTO")
    void shouldMapUserToUserDtoCorrectly() {
        final GetUserResponse mappedResult = toGetUserResponse(ALICE);

        assertThat(mappedResult).isEqualTo(ALICE_DTO);
    }

    @Test
    @DisplayName("should correctly map from UserDto to User")
    void shouldMapUserDtoToUserCorrectly() {
        final User mappedResult = toUser(ALICE_DTO);

        assertThat(mappedResult).isEqualTo(ALICE_USER_DTO);
    }

    @Test
    @DisplayName("should correctly map from UserSet to UserDTOSet")
    void shouldMapUserSetToUserDtoSetCorrectly() {
        final Set<GetUserResponse> mappedResult = toGetUsersResponse(Set.of(ALICE, BOB));

        assertThat(mappedResult).isEqualTo(Set.of(ALICE_DTO, BOB_DTO));
    }

    @Test
    @DisplayName("should correctly map from UserDTOSet to UserSet")
    void shouldMapUserDtoSetToUserSetCorrectly() {
        final Set<User> mappedResult = toUsers(Set.of(ALICE_DTO, BOB_DTO));

        assertThat(mappedResult).isEqualTo(Set.of(ALICE_USER_DTO, BOB_USER_DTO));
    }

    @Test
    @DisplayName("should correctly handle empty values for Set")
    void shouldReturnEmptySetWhenInputSetIsEmpty() {
        assertThat(toGetUsersResponse(Set.of())).isEmpty();
        assertThat(toUsers(Set.of())).isEmpty();
    }

    @Test
    @DisplayName("should map user with null token correctly")
    void shouldMapUserWithNullToken() {
        final User userWithNullToken = User.nullToken("some@example.com");

        final GetUserResponse dto = toGetUserResponse(userWithNullToken);

        assertThat(dto.username()).isEqualTo("some@example.com");
    }

}
