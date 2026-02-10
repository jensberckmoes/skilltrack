package com.sopra_steria.jens_berckmoes.domain.dto;

import java.util.Set;

public record GetAllUsersResponse(Set<GetUserResponse> getUserResponses) {}
