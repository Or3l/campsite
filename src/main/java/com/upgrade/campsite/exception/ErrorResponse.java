package com.upgrade.campsite.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Slf4j
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private HttpStatus httpStatus;
    private List<String> errors;

    public ErrorResponse(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }


}
