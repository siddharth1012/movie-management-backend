package com.siddharth.Hotel.Management.repository;

import com.siddharth.Hotel.Management.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    boolean existsByEventId(Integer id);
}
