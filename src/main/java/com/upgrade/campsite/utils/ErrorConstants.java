package com.upgrade.campsite.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorConstants {

    public static final String BOOKING_NOT_FOUND = "The booking cannot be found.";

    public static final String DATE_FORMAT = String.format("The dates has to be in format %s.", Constants.DATE_PATTERN);

    public static final String BOOKING_MAX_DAYS = String.format("The campsite can be reserved for max %d days.", Constants.MAX_STAY);

    public static final String RESERVATION_MAX_MONTH_AHEAD_MESSAGE = String.format("Reservations need to be made %d month ahead maximum.", Constants.RESERVATION_MAX_MONTH_AHEAD) ;

    public static final String RESERVATION_MIN_DAYS_AHEAD_MESSAGE = String.format("Reservations need to be made %d day ahead minimum.", Constants.RESERVATION_MIN_DAY_AHEAD);

    public static final String REQUEST_NOT_VALID = "The request is not valid.";

    public static final String DATE_NOT_AVAILABLE = "The selected dates are not available.";

    public static final String DEPARTURE_DATE_SAME_DAY_OR_BEFORE_ARRIVAL = "Arrival date cannot be after to departure date.";

    public static final String MISSING_DATE = "You need to specific arrival and departure";

    public static final String INTERNAL_ERROR = "There has been an error.";

}
