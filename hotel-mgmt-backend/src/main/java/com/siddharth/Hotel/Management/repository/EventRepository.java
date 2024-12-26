package com.siddharth.Hotel.Management.repository;

import com.siddharth.Hotel.Management.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    long countByEventType(String eventType);
    List<Event> findAllByEventType(String eventType);
    List<Event> findAllByName(String name);
}
