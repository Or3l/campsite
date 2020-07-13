package com.upgrade.campsite.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
@Builder
public class BookingPatchRequest {

    @Email(message = "Email is not valid")
    private String email;
    private String fullName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String arrival;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String departure;
}
