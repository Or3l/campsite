package com.upgrade.campsite.utils;

import com.upgrade.campsite.exception.BookingException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static com.upgrade.campsite.utils.ErrorConstants.DATE_FORMAT;
import static com.upgrade.campsite.utils.ErrorConstants.REQUEST_NOT_VALID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UtilsDate {

    public static LocalDate getLocalDateFromString(String date) {
        LocalDate localDate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_PATTERN);
            localDate = LocalDate.parse(date, formatter);
        } catch (RuntimeException e) {
            throw new BookingException(REQUEST_NOT_VALID, Arrays.asList(DATE_FORMAT));
        }
        return localDate;
    }

    public static LocalDate getLocalDateUTC() {
        return LocalDate.now(ZoneOffset.UTC);
    }
}
