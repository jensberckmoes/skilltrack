package com.sopra_steria.jens_berckmoes.advice;

import com.sopra_steria.jens_berckmoes.domain.exception.NoUsersFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoUsersFoundException.class)
    public ResponseEntity<Void> handleNoUsersFound(final NoUsersFoundException ex) {
        return ResponseEntity.noContent().build();
    }

}

