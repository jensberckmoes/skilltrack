package com.sopra_steria.jens_berckmoes.controller;

import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.dto.CreateUserRequest;
import com.sopra_steria.jens_berckmoes.domain.dto.GetUserResponse;
import com.sopra_steria.jens_berckmoes.domain.dto.GetAllUsersResponse;
import com.sopra_steria.jens_berckmoes.domain.exception.NoUsersFoundException;
import com.sopra_steria.jens_berckmoes.domain.mapping.UserDtoMapper;
import com.sopra_steria.jens_berckmoes.domain.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.domain.repository.UserRepository;
import com.sopra_steria.jens_berckmoes.domain.valueobject.Username;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/users")
public record UserController(UserRepository userRepository, TokenRepository tokenRepository) {

    @GetMapping
    public ResponseEntity<GetAllUsersResponse> getAllUsers() {
        final Set<User> all = userRepository.findAll();
        if(all.isEmpty()) {
            throw new NoUsersFoundException("No users found in the database.");
        }
        final GetAllUsersResponse response = new GetAllUsersResponse(UserDtoMapper.toGetUsersResponse(all));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = {"/", "/{username:.+}"})
    public ResponseEntity<GetUserResponse> getUserByUsername(@PathVariable(required = false) final String username) {
        final Username usernameValueObject = Username.of(username);
        final User user = userRepository.findByUsername(usernameValueObject);
        final GetUserResponse getUserResponse = UserDtoMapper.toGetUserResponse(user);
        return new ResponseEntity<>(getUserResponse, HttpStatus.OK);
    }

    public ResponseEntity<GetUserResponse> createUser(final CreateUserRequest request) {
        return new ResponseEntity<>(GetUserResponse.of(request.username()), HttpStatus.OK);
    }
}

