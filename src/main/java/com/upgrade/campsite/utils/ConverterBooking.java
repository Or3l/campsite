package com.upgrade.campsite.utils;

import com.upgrade.campsite.data.entity.Booking;
import com.upgrade.campsite.dto.BookingResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConverterBooking {

    public static BookingResponse convertEntity(Booking booking) {
        return BookingResponse.builder().arrival(booking.getArrival().toString()).departure(booking.getDeparture().toString()).bookingId(booking.getId()).email(booking.getEmail()).fullName(booking.getFullName()).build();
    }
}
