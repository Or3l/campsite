package com.upgrade.campsite.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingResponse {

    private UUID bookingId;
    private String email;
    private String fullName;
    private String arrival;
    private String departure;
}
