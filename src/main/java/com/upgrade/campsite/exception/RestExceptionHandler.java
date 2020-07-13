package com.upgrade.campsite.exception;

import com.upgrade.campsite.utils.ErrorConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

import static com.upgrade.campsite.utils.ErrorConstants.INTERNAL_ERROR;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Throwable exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, Arrays.asList(INTERNAL_ERROR));
        log.warn(HttpStatus.INTERNAL_SERVER_ERROR + " " + exception.getMessage(), exception);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(), HttpStatus.NOT_FOUND, Arrays.asList(exception.getMessage()));
        log.warn(HttpStatus.NOT_FOUND + " " + exception.getMessage(), exception);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleBookingException(BookingException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(), HttpStatus.BAD_REQUEST, exception.getConstraintViolations());
        log.warn(HttpStatus.BAD_REQUEST + " " + exception.getMessage(), exception);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleOptimisticLockingFailureException(OptimisticLockingFailureException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorConstants.REQUEST_NOT_VALID, HttpStatus.BAD_REQUEST, Arrays.asList(ErrorConstants.DATE_NOT_AVAILABLE));
        log.warn(HttpStatus.BAD_REQUEST + " " + exception.getMessage(), exception);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
