package com.siddharth.Hotel.Management.service.impl;

import com.siddharth.Hotel.Management.model.Event;
import com.siddharth.Hotel.Management.repository.BookingRepository;
import com.siddharth.Hotel.Management.repository.EventRepository;
import com.siddharth.Hotel.Management.repository.TheaterRepository;
import com.siddharth.Hotel.Management.service.EventService;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private TheaterRepository theaterRepo;

    Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    private boolean isOverLapping(LocalDateTime fromDate, LocalDateTime toDate) {
        List<Event> events = eventRepo.findAll();
        logger.info("Event is overlapping");
        for(Event event: events) {
            if(fromDate.isBefore(event.getToDate()) && toDate.isAfter(event.getFromDate())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Event createEvent(Event event) {
        LocalDateTime fromDate = event.getFromDate();
        LocalDateTime toDate = event.getToDate();
        if(isOverLapping(fromDate, toDate)) {
            throw new RuntimeException("Event Date overlaps with existing event.");
        }
        theaterRepo.save(event.getTheater());
        return eventRepo.save(event);
    }

    @Override
    public void deleteEvent(Integer id) {
        if(bookingRepo.existsByEventId(id)) {
            logger.info("Cannot Delete Event");
            throw new RuntimeException("Cannot Delete event with existing booking");
        }
        eventRepo.deleteById(id);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepo.findAll();
    }

    @Override
    public Event getEventById(Integer id) {
        return eventRepo.findById(id).orElse(null);
    }

    @Override
    public Page<Event> getPaginatedEvents(int pageNo, int pageSize, String sortBy, String sortOrder) {
        Sort sort = sortBy.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return eventRepo.findAll(pageable);
    }

    @Override
    public List<Event> getEventByName(String eventName) {
        return eventRepo.findAllByName(eventName);
    }

    @Override
    public List<Event> getEventByEventType(String eventType) {
        return eventRepo.findAllByEventType(eventType);
    }
}
