package com.upgrade.campsite.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String email;

    @Column(name = "full_name")
    private String fullName;

    private LocalDate arrival;

    private LocalDate departure;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    @OneToMany(mappedBy = "booking")
    private List<DateAvailability> availabilities;

    public void addDate(DateAvailability dateAvailability) {
        availabilities.add(dateAvailability);
        dateAvailability.setBooking(this);
        dateAvailability.setBooked(true);
    }

    public void removeDate(DateAvailability dateAvailability) {
        availabilities.remove(dateAvailability);
        dateAvailability.setBooking(null);
        dateAvailability.setBooked(false);
    }

    public void removeAllDate(){
        for(DateAvailability dateAvailability: availabilities){
            dateAvailability.setBooked(false);
            dateAvailability.setBooking(null);
        }

        availabilities = new ArrayList<>();
    }

}
