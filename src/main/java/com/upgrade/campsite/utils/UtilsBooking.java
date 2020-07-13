package com.upgrade.campsite.utils;

import com.upgrade.campsite.exception.BookingException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Set;

import static com.upgrade.campsite.utils.Constants.*;
import static com.upgrade.campsite.utils.ErrorConstants.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UtilsBooking {

    public static void checkStayLength(LocalDate arrival, LocalDate departure) {
        if (ChronoUnit.DAYS.between(arrival, departure) > MAX_STAY - 1) {
            throw new BookingException(REQUEST_NOT_VALID, Arrays.asList(BOOKING_MAX_DAYS));
        }
    }

    public static void checkIfBookedTooSoon(LocalDateTime now, LocalDateTime arrival) {
        if (ChronoUnit.MONTHS.between(now, arrival) > RESERVATION_MAX_MONTH_AHEAD || (ChronoUnit.MONTHS.between(now, arrival) == RESERVATION_MAX_MONTH_AHEAD && ChronoUnit.DAYS.between(now, arrival) != 0)) {
            throw new BookingException(REQUEST_NOT_VALID, Arrays.asList(RESERVATION_MAX_MONTH_AHEAD_MESSAGE));
        }
    }

    public static void checkIfBookedTooLate(LocalDateTime now, LocalDateTime arrival) {
        if (ChronoUnit.DAYS.between(now, arrival) < RESERVATION_MIN_DAY_AHEAD) {
            throw new BookingException(REQUEST_NOT_VALID, Arrays.asList(RESERVATION_MIN_DAYS_AHEAD_MESSAGE));
        }
    }

    public static void checkArrivalIsBeforeDeparture(LocalDate arrival, LocalDate departure) {
        if (arrival.isAfter(departure)) {
            throw new BookingException(REQUEST_NOT_VALID, Arrays.asList(DEPARTURE_DATE_SAME_DAY_OR_BEFORE_ARRIVAL));
        }
    }

    public static void checkAvailabilities(LocalDate arrival, LocalDate departure, Set<LocalDate> availabilities) {
        long days = ChronoUnit.DAYS.between(arrival, departure);
        LocalDate startDate = LocalDate.of(arrival.getYear(), arrival.getMonth(), arrival.getDayOfMonth());
        for (int i = 0; i <= days; i++) {
            LocalDate d = startDate.plusDays(i);
            if (!availabilities.contains(d)) {
                throw new BookingException(REQUEST_NOT_VALID, Arrays.asList(DATE_NOT_AVAILABLE));
            }
        }
    }
}
