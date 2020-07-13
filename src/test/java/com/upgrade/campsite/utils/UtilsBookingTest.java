package com.upgrade.campsite.utils;

import com.upgrade.campsite.exception.BookingException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UtilsBookingTest {

    @Test
    void checkStayLength_when_duration_too_long() {
        LocalDate arrival = LocalDate.of(2020, 07, 10);
        LocalDate departure = LocalDate.of(2020, 07, 13);
        BookingException bookingException = assertThrows(BookingException.class,
                () ->UtilsBooking.checkStayLength(arrival, departure));
        assertTrue(bookingException.getMessage().equals(ErrorConstants.REQUEST_NOT_VALID));
        assertTrue(bookingException.getConstraintViolations().get(0).equals(ErrorConstants.BOOKING_MAX_DAYS));
    }

    @Test
    void checkIfBookedTooSoon_when_booked_too_soon() {
        LocalDateTime arrival = LocalDateTime.of(2020, 07, 9, 0, 0);
        LocalDateTime now = LocalDateTime.of(2020, 06, 9, 0, 0);
        BookingException bookingException = assertThrows(BookingException.class,
                () ->UtilsBooking.checkIfBookedTooSoon(now, arrival));
        assertTrue(bookingException.getMessage().equals(ErrorConstants.REQUEST_NOT_VALID));
        assertTrue(bookingException.getConstraintViolations().get(0).equals(ErrorConstants.RESERVATION_MAX_MONTH_AHEAD_MESSAGE));
    }

    @Test
    void checkIfBookedTooLate_when_booked_too_late() {
        LocalDateTime arrival = LocalDateTime.of(2020, 07, 10, 0, 0);
        LocalDateTime now = LocalDateTime.of(2020, 07, 9, 1, 0);
        BookingException bookingException = assertThrows(BookingException.class,
                () ->UtilsBooking.checkIfBookedTooLate(now, arrival));
        assertTrue(bookingException.getMessage().equals(ErrorConstants.REQUEST_NOT_VALID));
        assertTrue(bookingException.getConstraintViolations().get(0).equals(ErrorConstants.RESERVATION_MIN_DAYS_AHEAD_MESSAGE));
    }

    @Test
    void checkArrivalIsBeforeDeparture_when_arrival_is_before_departure(){
        LocalDate arrival = LocalDate.of(2020, 07, 13);
        LocalDate departure = LocalDate.of(2020, 07, 11);
        BookingException bookingException = assertThrows(BookingException.class,
                () ->UtilsBooking.checkArrivalIsBeforeDeparture(arrival, departure));
        assertTrue(bookingException.getMessage().equals(ErrorConstants.REQUEST_NOT_VALID));
        assertTrue(bookingException.getConstraintViolations().get(0).equals(ErrorConstants.DEPARTURE_DATE_SAME_DAY_OR_BEFORE_ARRIVAL));
    }

    @Test
    void checkAvailabilities_when_not_available(){
        LocalDate arrival = LocalDate.of(2020, 07, 11);
        LocalDate departure = LocalDate.of(2020, 07, 13);
        Set<LocalDate> availabilities = new HashSet<>();
        availabilities.add(LocalDate.of(2020, 07, 13));
        BookingException bookingException = assertThrows(BookingException.class,
                ()->UtilsBooking.checkAvailabilities(arrival, departure, availabilities));
        assertTrue(bookingException.getMessage().equals(ErrorConstants.REQUEST_NOT_VALID));
        assertTrue(bookingException.getConstraintViolations().get(0).equals(ErrorConstants.DATE_NOT_AVAILABLE));
    }

    @Test
    void checkAvailabilities_when_available(){
        LocalDate arrival = LocalDate.of(2020, 07, 11);
        LocalDate departure = LocalDate.of(2020, 07, 13);
        Set<LocalDate> availabilities = new HashSet<>();
        availabilities.add(LocalDate.of(2020, 07, 13));
        availabilities.add(LocalDate.of(2020, 07, 12));
        availabilities.add(LocalDate.of(2020, 07, 11));
        UtilsBooking.checkAvailabilities(arrival, departure, availabilities);
    }
}