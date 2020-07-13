package com.upgrade.campsite.data.repository;

import com.upgrade.campsite.data.entity.Booking;
import com.upgrade.campsite.data.entity.DateAvailability;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DateAvailabilityRepository extends CrudRepository<DateAvailability, LocalDate> {
    List<DateAvailability> findAllByBookedFalseAndDayBetween(LocalDate arrival, LocalDate departure);

    List<DateAvailability> findAllByBooking(Booking booking);

    List<DateAvailability> findAllByDayBetween(LocalDate arrival, LocalDate departure);
}
