package com.upgrade.campsite.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingPostRequest {

    @Email(message = "Email is not valid")
    @NotEmpty(message = "Email cannot be blank")
    private String email;

    @NotEmpty(message = "FullName cannot be empty.")
    private String fullName;

    @NotEmpty
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String arrival;

    @NotEmpty(message = "Departure cannot be empty.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String departure;
}
