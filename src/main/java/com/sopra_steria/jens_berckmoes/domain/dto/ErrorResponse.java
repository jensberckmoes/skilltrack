package com.sopra_steria.jens_berckmoes.domain.dto;

public record ErrorResponse(
        String message,
        int status,
        String exception
) {}

