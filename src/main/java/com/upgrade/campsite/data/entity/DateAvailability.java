package com.upgrade.campsite.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DateAvailability {

    @Id
    LocalDate day;

    @Column(name = "IS_BOOKED")
    private boolean booked;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    private Booking booking;
}
