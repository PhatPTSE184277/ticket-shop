package com.ticketShop.persistence.mapper;

import com.ticketShop.model.entity.Event;
import com.ticketShop.model.enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventJPAMapper extends JpaRepository<Event, Long> {
    List<Event> findByStatus(EventStatus status);


    //check
    boolean existsByNameAndStartTimeAndEndTime(String name, LocalDateTime startTime, LocalDateTime endTime);
    boolean existsById(Long id);
}
