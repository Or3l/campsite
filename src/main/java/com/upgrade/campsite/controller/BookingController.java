package com.upgrade.campsite.controller;

import com.upgrade.campsite.data.entity.Booking;
import com.upgrade.campsite.dto.BookingPatchRequest;
import com.upgrade.campsite.dto.BookingPostRequest;
import com.upgrade.campsite.dto.BookingResponse;
import com.upgrade.campsite.service.BookingService;
import com.upgrade.campsite.utils.ConverterBooking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController()
public class BookingController {

    private BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping(value = "/bookings/availabilities")
    public List<String> getAvailabilities(@RequestParam(required = false) String start, @RequestParam(required = false) String end) {
        return bookingService.getAvailabilities(start, end).stream().map(LocalDate::toString).sorted().collect(Collectors.toList());
    }

    @PostMapping(value = "/bookings")
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingPostRequest bookingPostRequest) {
        Booking booking = bookingService.createBooking(bookingPostRequest);
        return new ResponseEntity<>(ConverterBooking.convertEntity(booking), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/bookings/{id}")
    public ResponseEntity<BookingResponse> updateBooking(@PathVariable UUID id, @RequestBody BookingPatchRequest bookingPatchRequest) {
        Booking booking = bookingService.updateBooking(id, bookingPatchRequest);
        return new ResponseEntity<>(ConverterBooking.convertEntity(booking), HttpStatus.OK);
    }

    @DeleteMapping(value = "/bookings/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable UUID id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/bookings/{id}")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable UUID id){
        Booking booking = bookingService.findBookingById(id);
        return new ResponseEntity<>(ConverterBooking.convertEntity(booking), HttpStatus.OK);
    }
}
