package com.sopra_steria.jens_berckmoes.controller;

import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.dto.UserDto;
import com.sopra_steria.jens_berckmoes.domain.dto.UserDtoResponse;
import com.sopra_steria.jens_berckmoes.domain.exception.NoUsersFoundException;
import com.sopra_steria.jens_berckmoes.domain.mapping.UserDtoMapper;
import com.sopra_steria.jens_berckmoes.domain.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.domain.repository.UserRepository;
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
    public ResponseEntity<UserDtoResponse> getAllUsers() {
        final Set<User> all = userRepository.findAll();
        if(all.isEmpty()) {
            throw new NoUsersFoundException();
        }
        final UserDtoResponse response = new UserDtoResponse(UserDtoMapper.toDtos(all));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{username:.+}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable final String username) {
        final User user = userRepository.findByUsername(username);
        final UserDto userDto = UserDtoMapper.toDto(user);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}

