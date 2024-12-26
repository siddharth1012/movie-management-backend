package com.siddharth.Hotel.Management.repository;

import com.siddharth.Hotel.Management.model.Booking;
import com.siddharth.Hotel.Management.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    boolean existsByCode(String code);
    Optional<Coupon> findByCode(String code);
}
