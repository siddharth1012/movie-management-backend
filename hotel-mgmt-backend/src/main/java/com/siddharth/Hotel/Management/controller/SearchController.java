package com.siddharth.Hotel.Management.controller;


import com.siddharth.Hotel.Management.model.Event;
import com.siddharth.Hotel.Management.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class SearchController {

    @Autowired
    private EventService eventService;

    Logger logger = LoggerFactory.getLogger(SearchController.class);

    @GetMapping(value = "/search-by-event-name/{eventName}")
    public ResponseEntity<List<Event>> getEventName(@PathVariable("eventName") String eventName) {
        logger.info("From Search Endpoint: searching for the events by event name");
        List<Event> events = eventService.getEventByName(eventName);
        return new ResponseEntity<List<Event>>(events, HttpStatus.OK);
    }

    @GetMapping(value = "/search-by-event-type/{eventType}")
    public ResponseEntity<List<Event>> getEventType(@PathVariable("eventType") String eventType) {
        logger.info("From search endpoints: Searching the events by event type");
        List<Event> events = eventService.getEventByEventType(eventType);
        return new ResponseEntity<List<Event>>(events, HttpStatus.OK);
    }

}
