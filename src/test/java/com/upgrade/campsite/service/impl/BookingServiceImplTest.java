package com.upgrade.campsite.service.impl;

import com.upgrade.campsite.data.entity.Booking;
import com.upgrade.campsite.data.repository.BookingRepository;
import com.upgrade.campsite.dto.BookingPatchRequest;
import com.upgrade.campsite.dto.BookingPostRequest;
import com.upgrade.campsite.utils.UtilsDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookingServiceImplTest {

    @SpyBean
    private BookingServiceImpl bookingService;

    @Autowired
    private BookingRepository bookingRepository;


    @Test
    @Sql(scripts = {"classpath:reset.sql"}, executionPhase = AFTER_TEST_METHOD)
    void createBooking_when_multiple_user_book_same_range() throws InterruptedException {
        String email = "test@email.com";
        String fullName = "fullName";
        LocalDate arrival = UtilsDate.getLocalDateUTC().plusDays(2);
        LocalDate departure = UtilsDate.getLocalDateUTC().plusDays(4);
        BookingPostRequest bookingPostRequest1 = BookingPostRequest.builder()
                .arrival(arrival.toString())
                .departure(departure.toString())
                .email(email)
                .fullName(fullName).build();

        BookingPostRequest bookingPostRequest2 = BookingPostRequest.builder()
                .arrival(arrival.plusDays(1).toString())
                .departure(departure.toString())
                .email(email)
                .fullName(fullName).build();

        BookingPostRequest bookingPostRequest3 = BookingPostRequest.builder()
                .arrival(arrival.plusDays(2).toString())
                .departure(departure.toString())
                .email(email)
                .fullName(fullName).build();



        List<BookingPostRequest> bookingPostRequestList = Arrays.asList(bookingPostRequest1, bookingPostRequest2, bookingPostRequest3);

        ExecutorService executorService = Executors.newFixedThreadPool(bookingPostRequestList.size());
        List<Callable<Booking>> callables = new ArrayList<>(bookingPostRequestList.size());
        for(BookingPostRequest bookingPostRequest: bookingPostRequestList){
            callables.add(()-> bookingService.createBooking(bookingPostRequest));
        }
        List<Future<Booking>> futures = executorService.invokeAll(callables);
        executorService.shutdown();
        for(Future<Booking> future: futures){
            try {
                future.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        List<Booking> booking = bookingRepository.findAllByArrivalBetweenAndDepartureBetween(arrival, departure);
        assertEquals(1, booking.size());
        verify(bookingService, times(3)).createBooking(any());
    }

    @Test
    @Sql(scripts = {"classpath:reset.sql"}, executionPhase = AFTER_TEST_METHOD)
    void updateBooking_when_multiple_user_same_range() throws InterruptedException {
        String email = "test@email.com";
        String fullName = "fullName";
        LocalDate arrival = UtilsDate.getLocalDateUTC().plusDays(2);
        LocalDate departure = UtilsDate.getLocalDateUTC().plusDays(4);

        BookingPostRequest bookingPostRequest1 = BookingPostRequest.builder()
                .arrival(arrival.toString())
                .departure(departure.toString())
                .email(email)
                .fullName(fullName).build();

        BookingPostRequest bookingPostRequest2 = BookingPostRequest.builder()
                .arrival(arrival.plusDays(5).toString())
                .departure(departure.plusDays(5).toString())
                .email(email)
                .fullName(fullName).build();

        Booking booking1 = bookingService.createBooking(bookingPostRequest1);
        Booking booking2 = bookingService.createBooking(bookingPostRequest2);

        LocalDate newArrival = UtilsDate.getLocalDateUTC().plusDays(10);
        LocalDate newDeparture = UtilsDate.getLocalDateUTC().plusDays(10);
        BookingPatchRequest bookingPatchRequest1 = BookingPatchRequest.builder().arrival(newArrival.toString()).departure(newDeparture.toString()).build();
        BookingPatchRequest bookingPatchRequest2 = BookingPatchRequest.builder().arrival(newArrival.toString()).departure(newDeparture.toString()).build();

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<Callable<Booking>> callables = new ArrayList<>(2);

        callables.add(()-> bookingService.updateBooking(booking1.getId(), bookingPatchRequest1));
        callables.add(()-> bookingService.updateBooking(booking2.getId(), bookingPatchRequest2));

        List<Future<Booking>> futures = executorService.invokeAll(callables);
        executorService.shutdown();
        for(Future<Booking> future: futures){
            try {
                future.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        List<Booking> booking = bookingRepository.findAllByArrivalBetweenAndDepartureBetween(newArrival, newDeparture);
        assertEquals(1, booking.size());
        verify(bookingService, times(2)).updateBooking(any(), any());
    }
}