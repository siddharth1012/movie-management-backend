package com.siddharth.Hotel.Management.controller;


import com.siddharth.Hotel.Management.model.Coupon;
import com.siddharth.Hotel.Management.model.Event;
import com.siddharth.Hotel.Management.service.CouponService;
import com.siddharth.Hotel.Management.service.EventService;
import com.siddharth.Hotel.Management.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private EventService eventService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private CouponService couponService;

    Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("/events")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        logger.info("Events Endpoint: Getting all the events");
        return new ResponseEntity<List<Event>>(events, HttpStatus.OK);
    }

    @PostMapping(value = "/events")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event createdEvent = eventService.createEvent(event);
        logger.info("Creating event in events endpoint");
        return new ResponseEntity<Event>(createdEvent, HttpStatus.OK);
    }

    @DeleteMapping(value = "/events/{id}")
    public String deleteEvent(@PathVariable("id") Integer id) {
        logger.info("Deleted event throught the deleteEvent Endpoint");
        eventService.deleteEvent(id);
        return "Event deleted successfully";
    }

    @GetMapping(value = "/report/total-bookings")
    public ResponseEntity<Double> getTotalBookings() {
        logger.info("Get total bookings from the report endpoints");
        Double totalBookings = reportService.getTotalBookings();
        return new ResponseEntity<Double>(totalBookings, HttpStatus.OK);
    }

    @GetMapping(value = "/report/total-gst")
    public ResponseEntity<Double> getTotalGst() {
        logger.info("Get total GST from the report endpoints");
        Double totalGst = reportService.getGstCollected();
        return new ResponseEntity<Double>(totalGst, HttpStatus.OK);
    }

    @GetMapping(value = "/report/total-amount")
    public ResponseEntity<Double> getTotalAmount() {
        logger.info("Get total Amount from the report endpoints");
        Double totalAmount = reportService.getTotalAmount();
        return new ResponseEntity<Double>(totalAmount, HttpStatus.OK);
    }

    @GetMapping(value = "/report/count-by-theater/{name}")
    public ResponseEntity<Long> bookingCountByTheater(@PathVariable("name") String name) {
        logger.info("Returns the number of events at a theater");
        Long bookingCount = reportService.getBookingCountByTheater(name);
        return new ResponseEntity<Long>(bookingCount, HttpStatus.OK);
    }

    @GetMapping(value = "/report/count-by-event-type/{eventType}")
    public ResponseEntity<Long> bookingCountByEventType(@PathVariable("eventType") String eventType) {
        logger.info("Returns the number of event types");
        Long bookingCount = reportService.getBookingCountByEventType(eventType);
        return new ResponseEntity<Long>(bookingCount, HttpStatus.OK);
    }

    @PostMapping("/coupons")
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon) {
        logger.info("Create a coupon from coupon endpoint");
        Coupon createdCoupon = couponService.createCoupon(coupon);
        return new ResponseEntity<Coupon>(createdCoupon, HttpStatus.OK);
    }

    @DeleteMapping("/coupons/{id}")
    public String deleteCoupon(@PathVariable("id") Integer id) {
        logger.info("Deleted a coupon from the coupon endpoint");
        couponService.deleteCoupon(id);
        return "Coupon Deleted";
    }
}
