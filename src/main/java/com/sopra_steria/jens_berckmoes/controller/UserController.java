package com.sopra_steria.jens_berckmoes.controller;

import com.sopra_steria.jens_berckmoes.domain.dto.UserDto;
import com.sopra_steria.jens_berckmoes.domain.mapping.UserDtoMapper;
import com.sopra_steria.jens_berckmoes.domain.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.domain.repository.UserRepository;

import java.util.Set;

public record UserController( UserRepository userRepository, TokenRepository tokenRepository) {
    public Set<UserDto> getAllUsers() {
        return UserDtoMapper.toDto(userRepository.findALl());
    }
}

