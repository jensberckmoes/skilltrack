package com.sopra_steria.jens_berckmoes.domain;

public record ErrorResponse(
        String message,
        int status,
        String exception
) {}

