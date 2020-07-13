package com.upgrade.campsite.data.repository;

import com.upgrade.campsite.data.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    @Query("SELECT b FROM Booking b where b.arrival BETWEEN ?1 AND ?2 OR b.departure between ?1 AND ?2")
    List<Booking> findAllByArrivalBetweenAndDepartureBetween(LocalDate arrival, LocalDate departure);
}
