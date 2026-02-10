package com.sopra_steria.jens_berckmoes.advice;

import com.sopra_steria.jens_berckmoes.domain.dto.ErrorResponse;
import com.sopra_steria.jens_berckmoes.domain.exception.NoUsersFoundException;
import com.sopra_steria.jens_berckmoes.domain.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.domain.exception.UsernameRawValueNullOrBlankException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoUsersFoundException.class)
    public ResponseEntity<Void> handleNoUsersFound(final NoUsersFoundException ex) {
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(UsernameRawValueNullOrBlankException.class)
    public ResponseEntity<ErrorResponse> handleUserInvalidUsernameEntered(final UsernameRawValueNullOrBlankException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), ex.getClass().getSimpleName()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(final UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value(), ex.getClass().getSimpleName()));
    }


}

