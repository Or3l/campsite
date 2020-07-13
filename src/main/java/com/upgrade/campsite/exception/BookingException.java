package com.upgrade.campsite.exception;

import lombok.Data;

import java.util.List;

@Data
public class BookingException extends RuntimeException {

    private List<String> constraintViolations;

    public BookingException(String message, List<String> constraintViolations) {
        super(message);
        this.constraintViolations = constraintViolations;
    }


}
