package com.ticketShop.repository;

import com.ticketShop.model.entity.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository {
    List<Event> findAllActive();

    /**
     * Lưu event mới
     * @param event
     * @return Ticket được lưu (kèm ID)
     */
    Event save(Event event);


    //Check
    boolean existsByNameAndStartTimeAndEndTime(String name, LocalDateTime startTime, LocalDateTime endTime);
    boolean existsById(Long id);
}
