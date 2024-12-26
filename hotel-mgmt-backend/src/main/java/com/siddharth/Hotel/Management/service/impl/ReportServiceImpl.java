package com.siddharth.Hotel.Management.service.impl;

import com.siddharth.Hotel.Management.model.Booking;
import com.siddharth.Hotel.Management.repository.BookingRepository;
import com.siddharth.Hotel.Management.repository.EventRepository;
import com.siddharth.Hotel.Management.repository.TheaterRepository;
import com.siddharth.Hotel.Management.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private TheaterRepository theaterRepo;

    @Autowired
    private EventRepository eventRepo;

    @Override
    public double getTotalBookings() {
        return bookingRepo.count();
    }

    @Override
    public double getGstCollected() {
        List<Booking> bookings = bookingRepo.findAll();
        double gst = 0.0;
        for(Booking booking: bookings) {
            gst = gst + booking.getGST();
        }
        return gst;
    }

    @Override
    public double getTotalAmount() {
        List<Booking> bookings = bookingRepo.findAll();
        double totalAmount = 0.0;
        for(Booking booking: bookings) {
            totalAmount = totalAmount + booking.getTotalAmount();
        }
        return totalAmount;
    }

    @Override
    public long getBookingCountByTheater(String name) {
        return theaterRepo.countByName(name);
    }

    @Override
    public long getBookingCountByEventType(String eventType) {
        return eventRepo.countByEventType(eventType);
    }
}
