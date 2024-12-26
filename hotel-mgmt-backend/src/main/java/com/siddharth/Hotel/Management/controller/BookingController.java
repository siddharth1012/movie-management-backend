package com.siddharth.Hotel.Management.controller;


import com.siddharth.Hotel.Management.model.Booking;
import com.siddharth.Hotel.Management.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class BookingController {
    Logger logger = LoggerFactory.getLogger(BookingController.class);
    @Autowired
    private BookingService bookingService;

    @PostMapping(value = "/bookings")
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        Booking createdBooking = bookingService.createBooking(booking);
        logger.info("Booking endpoint for booking creation is called and booking is created");
        return new ResponseEntity<Booking>(createdBooking, HttpStatus.OK);
    }

    @GetMapping(value = "/bookings")
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        logger.info("Returning a list of bookings");
        return new ResponseEntity<List<Booking>>(bookings, HttpStatus.OK);
    }

    @DeleteMapping(value = "/bookings/{id}")
    public String deleteBooking(@PathVariable("id") Integer id) {
        bookingService.deleteBooking(id);
        logger.info("Booking deleted successfully");
        return "Booking deleted successfully";
    }

    @PostMapping(value = "/bookings/cancel/{id}")
    public ResponseEntity<Double> cancelBooking(@PathVariable("id") Integer id) {
        Double refund = bookingService.cancelBooking(id);
        logger.info("From booking endpoint: cancelling booking");
        return new ResponseEntity<Double>(refund, HttpStatus.OK);
    }

    @PostMapping(value = "/bookings/partial-cancel/{id}")
    public ResponseEntity<Double> particalCancel(@PathVariable("id") Integer id, @RequestBody Booking booking) {
        Double refund = bookingService.partialCancelBooking(id, booking);
        logger.info("From booking endpoint: Patial cancellation is done");
        return new ResponseEntity<Double>(refund, HttpStatus.OK);
    }
}
