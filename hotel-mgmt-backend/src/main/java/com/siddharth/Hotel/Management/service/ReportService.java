package com.siddharth.Hotel.Management.service;

public interface ReportService {
    double getTotalBookings();
    double getGstCollected();
    double getTotalAmount();
    long getBookingCountByTheater(String name);
    long getBookingCountByEventType(String eventType);
}
