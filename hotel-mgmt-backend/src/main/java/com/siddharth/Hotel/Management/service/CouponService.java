package com.siddharth.Hotel.Management.service;

import com.siddharth.Hotel.Management.model.Coupon;

public interface CouponService {
    Coupon createCoupon(Coupon coupon);
    void deleteCoupon(Integer id);
}

