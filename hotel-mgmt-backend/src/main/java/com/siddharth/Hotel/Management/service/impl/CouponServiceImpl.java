package com.siddharth.Hotel.Management.service.impl;

import com.siddharth.Hotel.Management.model.Coupon;
import com.siddharth.Hotel.Management.repository.CouponRepository;
import com.siddharth.Hotel.Management.service.CouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouponServiceImpl implements CouponService {
    @Autowired
    private CouponRepository couponRepo;
    Logger logger = LoggerFactory.getLogger(CouponServiceImpl.class);

    @Override
    public Coupon createCoupon(Coupon coupon) {
        logger.info("Created Coupon in Coupon service");
        return couponRepo.save(coupon);
    }

    @Override
    public void deleteCoupon(Integer id) {
        logger.info("Delete Coupon is called in CouponServiceImpl");
        couponRepo.deleteById(id);
    }
}
