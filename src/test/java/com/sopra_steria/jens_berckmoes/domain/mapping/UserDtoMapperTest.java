package com.sopra_steria.jens_berckmoes.domain.mapping;

import com.sopra_steria.jens_berckmoes.TestConstants;
import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.dto.UserDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.sopra_steria.jens_berckmoes.TestConstants.UserDtos.VALID_USER_DTO_FOR_ONE_MORE_DAY;
import static com.sopra_steria.jens_berckmoes.TestConstants.UserDtos.VALID_USER_DTO_FOR_TEN_YEARS;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.*;
import static com.sopra_steria.jens_berckmoes.domain.User.nullToken;
import static com.sopra_steria.jens_berckmoes.domain.mapping.UserDtoMapper.*;

@DisplayName("UserDtoMapper")
public class UserDtoMapperTest {
    @Test
    @DisplayName("should correctly map from User to UserDTO")
    void shouldMapUserToUserDtoCorrectly() {
        final UserDto mappedResult = toDto(VALID_USER_FOR_TEN_YEAR);

        Assertions.assertThat(mappedResult).isEqualTo(VALID_USER_DTO_FOR_TEN_YEARS);
    }

    @Test
    @DisplayName("should correctly map from UserDto to User")
    void shouldMapUserDtoToUserCorrectly() {
        final User mappedResult = toUser(VALID_USER_DTO_FOR_TEN_YEARS);

        Assertions.assertThat(mappedResult)
                .isEqualTo(nullToken(VALID_USERNAME_FOR_TEN_YEARS_RAW_STRING));
    }

    @Test
    @DisplayName("should correctly map from UserSet to UserDTOSet")
    void shouldMapUserSetToUserDtoSetCorrectly() {
        final Set<UserDto> mappedResult = toDto(Set.of(VALID_USER_FOR_TEN_YEAR, VALID_USER_FOR_ONE_MORE_DAY));

        Assertions.assertThat(mappedResult).isEqualTo(Set.of(VALID_USER_DTO_FOR_TEN_YEARS, VALID_USER_DTO_FOR_ONE_MORE_DAY));
    }

    @Test
    @DisplayName("should correctly map from UserDTOSet to UserSet")
    void shouldMapUserDtoSetToUserSetCorrectly() {
        final Set<User> mappedResult = toUsers(Set.of(VALID_USER_DTO_FOR_TEN_YEARS, VALID_USER_DTO_FOR_ONE_MORE_DAY));

        Assertions.assertThat(mappedResult).isEqualTo(Set.of(nullToken(VALID_USERNAME_FOR_TEN_YEARS_RAW_STRING), nullToken(VALID_USERNAME_FOR_ONE_MORE_DAY_RAW_STRING)));
    }

}
