package com.upgrade.campsite.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingPatchRequest {

    @Email(message = "Email is not valid")
    private String email;
    private String fullName;
    @Pattern(regexp = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))", message = "The dates has to be in format yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String arrival;
    @Pattern(regexp = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))", message = "The dates has to be in format yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String departure;
}
