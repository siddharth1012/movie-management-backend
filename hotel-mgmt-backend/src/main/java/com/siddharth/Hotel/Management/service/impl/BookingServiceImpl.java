package com.siddharth.Hotel.Management.service.impl;

import com.siddharth.Hotel.Management.model.Booking;
import com.siddharth.Hotel.Management.model.Coupon;
import com.siddharth.Hotel.Management.model.Event;
import com.siddharth.Hotel.Management.model.Users;
import com.siddharth.Hotel.Management.repository.BookingRepository;
import com.siddharth.Hotel.Management.repository.CouponRepository;
import com.siddharth.Hotel.Management.repository.EventRepository;
import com.siddharth.Hotel.Management.repository.UserRepository;
import com.siddharth.Hotel.Management.service.BookingService;
import com.siddharth.Hotel.Management.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepo;
    @Autowired
    private EventService eventService;
    @Autowired
    private EventRepository eventRepo;
//    @Autowired
//    private UserDetailsService userDetailsService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CouponRepository couponRepo;

    Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

    @Override
    public Booking createBooking(Booking booking) {
        Integer availableSeats = eventService.getEventById(booking.getEventId()).getMaxOccupancy();

        if(booking.getSeatsBooked() > availableSeats) {
            logger.error("Trying to book more than available seats");
            throw new RuntimeException("Trying to book more than available seats");
        }

        booking.setIsPartiallyCancelled(false);
        booking.setIsCancelled(false);

        Double discount = 1.0;

        String code = booking.getCode();
        Coupon coupon = couponRepo.findByCode(code).orElse(null);

        if(coupon != null) {
            LocalDate expiryDate = coupon.getExpiryDate();
            if(expiryDate.isBefore(LocalDate.now()) && coupon.getMaxUsage() <= coupon.getCurrentUsage()) {
                discount = 1.0;
            }
            else {
                logger.info("Applying discount for the booking");
                discount = coupon.getDiscountPercentages()/100;
                coupon.setCurrentUsage(coupon.getCurrentUsage() + 1);
                couponRepo.save(coupon);
            }
        }

        DayOfWeek dayOfWeek = eventService.getEventById(booking.getEventId()).getFromDate().getDayOfWeek();

        double discountedPrice = eventService.getEventById(booking.getEventId()).getPrice()*discount*booking.getSeatsBooked();

        double amt = (eventService.getEventById(booking.getEventId()).getPrice())*booking.getSeatsBooked() - discountedPrice;

        Event currentEvent = eventService.getEventById(booking.getEventId());

        double GST = 0.0;

        if(currentEvent.getEventType().equals("Movies")) {
            GST = amt*(0.08);
        }
        else if(currentEvent.getEventType().equals("Concerts")) {
            GST = amt*(0.1);
        }
        else {
            GST = amt*0.06;
        }

        double totalAmount = amt + GST;

        if(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            totalAmount = totalAmount*(1.25);
            logger.info("Surging prices for the booking on weekends");
        }

        Users currentUser = userRepo.findByName(booking.getUserId());
        LocalDate currentUserAge = currentUser.getDateOfBirth();
        LocalDate timeNow = LocalDate.now();
        if(ChronoUnit.YEARS.between(currentUserAge, timeNow) >= 60) {
            booking.setGST(0.0);
            logger.info("Age More than 60, setting the GST to 0");
        }
        else {
            booking.setGST(GST);
        }

        booking.setAmount(amt);
        booking.setTotalAmount(totalAmount);
        booking.setAmountRefunded(0.0);
        Integer updatedSeats = currentEvent.getMaxOccupancy() - booking.getSeatsBooked();
        currentEvent.setMaxOccupancy(updatedSeats);
        eventRepo.save(currentEvent);
        logger.info("Commiting the event to the eventRepo from Create Booking function in the booking service");
        return bookingRepo.save(booking);
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepo.findAll();
    }

    @Override
    public void deleteBooking(Integer id) {
        logger.info("From Booking Service: Delete booking function is called");
        bookingRepo.deleteById(id);
    }

    @Override
    public Double cancelBooking(Integer id) {
        Booking currentBooking = bookingRepo.findById(id).orElseThrow(() -> new RuntimeException("Booking Not Found"));
        Event event = eventService.getEventById(currentBooking.getEventId());
        LocalDateTime eventStartTime = event.getFromDate();
        LocalDateTime now = LocalDateTime.now();

        long hoursRemaining = ChronoUnit.HOURS.between(now, eventStartTime);

        if(hoursRemaining < 2) {
            logger.info("Less than two hours remaining before event");
            throw new RuntimeException("Cancellation not allowed within 2 hours of the event start time");
        }

        double refundableAmount = 0.0;
        double amountPaid = currentBooking.getTotalAmount();

        if(hoursRemaining >= 24) {
            refundableAmount = amountPaid*0.80;
        }
        else if(hoursRemaining >= 12) {
            refundableAmount = amountPaid*0.50;
        }
        else {
            refundableAmount = amountPaid*0.10;
        }

        int seatsReleased = currentBooking.getSeatsBooked();
        int revisedSeatOccupancy = event.getMaxOccupancy() + seatsReleased;
        event.setMaxOccupancy(revisedSeatOccupancy);
        logger.info("Saving the updated seats to the event repo");
        eventRepo.save(event);
        currentBooking.setIsCancelled(true);
        currentBooking.setAmountRefunded(refundableAmount);
        bookingRepo.save(currentBooking);
        logger.info("Commiting the current booking to the booking repo with the cancellation true");
        return refundableAmount;
    }

    @Override
    public Double partialCancelBooking(Integer id, Booking updateBooking) {
        Booking currentBooking  = bookingRepo.findById(id).orElseThrow(() -> new RuntimeException("Booking Not Found"));
        Event event = eventService.getEventById(currentBooking.getEventId());
        LocalDateTime eventStartTime = event.getFromDate();
        LocalDateTime now = LocalDateTime.now();

        long hoursRemaining = ChronoUnit.HOURS.between(now, eventStartTime);

        if(hoursRemaining < 2) {
            logger.error("Hours remaining are less than two. Cancellation cannot be done");
            throw new RuntimeException("Cancellation not allowed within 2 hours of the event start time");
        }

        double refundableAmount = 0.0;
        int revisedSeats = updateBooking.getSeatsBooked();
        int releasedSeats = currentBooking.getSeatsBooked()-revisedSeats;
        double newAmountPaid = revisedSeats*event.getPrice();

        if(hoursRemaining >= 24) {
            refundableAmount = event.getPrice()*0.80*releasedSeats;
        }
        else if(hoursRemaining >= 12) {
            refundableAmount = event.getPrice()*0.50*releasedSeats;
        }
        else {
            refundableAmount = event.getPrice()*0.10*releasedSeats;
        }

        int revisedSeatOccupancy = event.getMaxOccupancy() + releasedSeats;
        event.setMaxOccupancy(revisedSeatOccupancy);
        logger.info("Saving the event to the database");
        eventRepo.save(event);
        currentBooking.setSeatsBooked(revisedSeats);
        currentBooking.setIsPartiallyCancelled(true);
        currentBooking.setTotalAmount(newAmountPaid);
        currentBooking.setAmountRefunded(refundableAmount);
        bookingRepo.save(currentBooking);
        return refundableAmount;
    }
}
