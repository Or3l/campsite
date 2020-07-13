package com.upgrade.campsite.service.impl;

import com.upgrade.campsite.data.entity.Booking;
import com.upgrade.campsite.data.entity.DateAvailability;
import com.upgrade.campsite.data.repository.BookingRepository;
import com.upgrade.campsite.data.repository.DateAvailabilityRepository;
import com.upgrade.campsite.dto.BookingPatchRequest;
import com.upgrade.campsite.dto.BookingPostRequest;
import com.upgrade.campsite.exception.BookingException;
import com.upgrade.campsite.exception.NotFoundException;
import com.upgrade.campsite.service.BookingService;
import com.upgrade.campsite.utils.UtilsDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

import static com.upgrade.campsite.utils.Constants.DEFAULT_SEARCH_RANGE;
import static com.upgrade.campsite.utils.ErrorConstants.*;
import static com.upgrade.campsite.utils.UtilsBooking.*;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {

    private BookingRepository bookingRepository;

    private DateAvailabilityRepository dateAvailabilityRepository;

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, DateAvailabilityRepository dateAvailabilityRepository) {
        this.bookingRepository = bookingRepository;
        this.dateAvailabilityRepository = dateAvailabilityRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Set<LocalDate> getAvailabilities(String start, String end) {
        List<DateAvailability> dateAvailabilities = getDateAvailabilitiesNotBooked(start, end);
        return dateAvailabilities.stream().map(DateAvailability::getDay).collect(Collectors.toSet());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Booking createBooking(BookingPostRequest bookingPostRequest) {
        Set<ConstraintViolation<BookingPostRequest>> violations = validator.validate(bookingPostRequest);
        if (!violations.isEmpty()) {
            throw new BookingException(REQUEST_NOT_VALID, violations.stream().map(v -> v.getMessage()).collect(Collectors.toList()));
        }
        LocalDate arrival = UtilsDate.getLocalDateFromString(bookingPostRequest.getArrival());
        LocalDate departure = UtilsDate.getLocalDateFromString(bookingPostRequest.getDeparture());

        Set<LocalDate> availabilities = getAvailabilities(arrival.toString(), departure.toString());
        validateBooking(arrival, departure, availabilities);

        List<DateAvailability> dateAvailabilities = dateAvailabilityRepository.findAllByDayBetween(arrival, departure);
        Booking booking = Booking.builder().arrival(arrival).departure(departure)
                .email(bookingPostRequest.getEmail())
                .fullName(bookingPostRequest
                        .getFullName()).availabilities(new ArrayList<>(dateAvailabilities.size())).build();

        dateAvailabilities.stream().forEach(d ->
            booking.addDate(d)
        );

        return bookingRepository.save(booking);
    }

    @Transactional
    @Override
    public void deleteBooking(UUID bookingId) {
        Booking booking = findBookingById(bookingId);
        List<DateAvailability> dateAvailabilities = dateAvailabilityRepository.findAllByDayBetween(booking.getArrival(), booking.getDeparture());
        dateAvailabilities.stream().forEach(d-> booking.removeDate(d));
        dateAvailabilityRepository.saveAll(dateAvailabilities);
        bookingRepository.delete(booking);
    }

    @Transactional
    @Override
    public Booking updateBooking(UUID bookingId, BookingPatchRequest bookingPatchRequest) {
        Booking booking = findBookingById(bookingId);
        LocalDate arrival = booking.getArrival();
        LocalDate departure = booking.getDeparture();

        if (bookingPatchRequest.getArrival() != null) {
            arrival = UtilsDate.getLocalDateFromString(bookingPatchRequest.getArrival());
        }

        if (bookingPatchRequest.getDeparture() != null) {
            departure = UtilsDate.getLocalDateFromString(bookingPatchRequest.getDeparture());
        }

        if (bookingPatchRequest.getDeparture() != null || bookingPatchRequest.getArrival() != null) {
            List<DateAvailability> dateAvailabilities = getDateAvailabilitiesNotBooked(arrival.toString(), departure.toString());
            dateAvailabilities.addAll(booking.getAvailabilities());

            Set<LocalDate> availabilities = dateAvailabilities.stream().map(DateAvailability::getDay).collect(Collectors.toSet());

            validateBooking(arrival, departure, availabilities);

            booking.removeAllDate();

            List<DateAvailability> newDateAvailabilities = dateAvailabilityRepository.findAllByDayBetween(arrival, departure);
            newDateAvailabilities.stream().forEach(d -> booking.addDate(d));

            booking.setArrival(arrival);
            booking.setDeparture(departure);
        }

        if (bookingPatchRequest.getEmail() != null) {
            booking.setEmail(bookingPatchRequest.getEmail());
        }
        if (bookingPatchRequest.getFullName() != null) {
            booking.setFullName(bookingPatchRequest.getFullName());
        }
        booking.setUpdatedAt(UtilsDate.getLocalDateUTC());
        return bookingRepository.save(booking);
    }


    private List<DateAvailability> getDateAvailabilitiesNotBooked(String start, String end){
        LocalDate arrival, departure;

        if ((start == null && end != null) || (start != null && end == null)) {
            throw new BookingException(REQUEST_NOT_VALID, Arrays.asList(MISSING_DATE));
        }
        if (start == null && end == null) {
            arrival = UtilsDate.getLocalDateUTC();
            departure = arrival.plusMonths(DEFAULT_SEARCH_RANGE);
        } else {
            arrival = UtilsDate.getLocalDateFromString(start);
            departure = UtilsDate.getLocalDateFromString(end);
        }

        checkArrivalIsBeforeDeparture(arrival, departure);
        return dateAvailabilityRepository.findAllByBookedFalseAndDayBetween(arrival, departure);
    }


    private Booking findBookingById(UUID bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException(BOOKING_NOT_FOUND));
    }

    private void validateBooking(LocalDate arrival, LocalDate departure, Set<LocalDate> availabilities) {
        LocalDate now = UtilsDate.getLocalDateUTC();
        checkArrivalIsBeforeDeparture(arrival, departure);
        checkStayLength(arrival, departure);
        checkIfBookedTooLate(now.atTime(LocalTime.now(ZoneOffset.UTC)), arrival.atTime(LocalTime.MIDNIGHT));
        checkIfBookedTooSoon(now.atTime(LocalTime.now(ZoneOffset.UTC)), arrival.atTime(LocalTime.MIDNIGHT));
        checkAvailabilities(arrival, departure, availabilities);
    }
}
