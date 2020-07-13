package com.upgrade.campsite.service;

import com.upgrade.campsite.data.entity.Booking;
import com.upgrade.campsite.dto.BookingPatchRequest;
import com.upgrade.campsite.dto.BookingPostRequest;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public interface BookingService {

    Set<LocalDate> getAvailabilities(String start, String end);

    Booking createBooking(BookingPostRequest bookingPostRequest);

    void deleteBooking(UUID bookingId);

    Booking updateBooking(UUID bookingId, BookingPatchRequest bookingPatchRequest);

}
