package com.siddharth.Hotel.Management.service;

import com.siddharth.Hotel.Management.model.Booking;

import java.util.List;

public interface BookingService {
    Booking createBooking(Booking booking);
    List<Booking> getAllBookings();
    void deleteBooking(Integer id);
    Double cancelBooking(Integer id);
    Double partialCancelBooking(Integer id, Booking updateBooking);
}
