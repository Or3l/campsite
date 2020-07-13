package com.upgrade.campsite.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.upgrade.campsite.utils.ErrorConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingPostRequest {

    public static final String DATE_FORMAT = ErrorConstants.DATE_FORMAT;

    @Email(message = "Email is not valid")
    @NotEmpty(message = "Email cannot be blank")
    private String email;

    @NotEmpty(message = "FullName cannot be empty.")
    private String fullName;

    @NotEmpty(message = "arrival cannot be empty.")
    @Pattern(regexp = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))", message = "The dates has to be in format yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String arrival;

    @NotEmpty(message = "Departure cannot be empty.")
    @Pattern(regexp = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))", message = "The dates has to be in format yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String departure;
}
