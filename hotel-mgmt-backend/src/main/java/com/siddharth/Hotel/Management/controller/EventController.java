package com.siddharth.Hotel.Management.controller;

import com.siddharth.Hotel.Management.model.Event;
import com.siddharth.Hotel.Management.repository.BookingRepository;
import com.siddharth.Hotel.Management.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class EventController {
    @Autowired
    private EventService eventService;
    @Autowired
    private BookingRepository bookingRepo;

    Logger logger = LoggerFactory.getLogger(EventController.class);

    @PostMapping(value = "/events")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event createdEvent = eventService.createEvent(event);
        logger.info("From events endpoint: event is created");
        return new ResponseEntity<Event>(event, HttpStatus.OK);
    }

    @DeleteMapping(value = "/events/{id}")
    public String deleteEvent(@PathVariable("id") Integer id) {
        if(bookingRepo.existsByEventId(id)) {
            logger.error("Tried deleting the events with associated booking");
            throw new RuntimeException("Cannot delete events with existing bookings");
        }
        eventService.deleteEvent(id);
        logger.info("From event controller: An event is deleted");
        return "Event Deleted successfully";
    }

    @GetMapping(value = "/paginatedView")
    public ResponseEntity<Page<Event>> getPaginatedEvents(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder
    ) {
        Page<Event> events = eventService.getPaginatedEvents(pageNo, pageSize, sortBy, sortOrder);
        logger.info("From event controller: Creates the Pages");
        return new ResponseEntity<Page<Event>>(events, HttpStatus.OK);
    }
}
