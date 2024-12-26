package com.siddharth.Hotel.Management.service;

import com.siddharth.Hotel.Management.model.Event;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EventService {
    Event createEvent(Event event);
    void deleteEvent(Integer id);
    List<Event> getAllEvents();
    Event getEventById(Integer id);
    Page<Event> getPaginatedEvents(int pageNo, int pageSize, String sortBy, String sortOrder);
    List<Event> getEventByName(String eventName);
    List<Event> getEventByEventType(String eventType);

}
