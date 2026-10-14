package com.ticketShop.persistence.repository;

import com.ticketShop.model.entity.Event;
import com.ticketShop.model.enums.EventStatus;
import com.ticketShop.persistence.mapper.EventJPAMapper;
import com.ticketShop.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Slf4j
public class EventRepositoryImpl implements EventRepository {
    @Autowired
    private EventJPAMapper eventJPAMapper;

    @Override
    public List<Event> findAllActive() {
        return eventJPAMapper.findByStatus(EventStatus.ACTIVE);
    }

    @Override
    public Event save(Event event) {
        log.info("Saving Event: {}", event.getName());
        return eventJPAMapper.save(event);
    }


    //Check
    @Override
    public boolean existsByNameAndStartTimeAndEndTime(String name, LocalDateTime startTime, LocalDateTime endTime) {
        return eventJPAMapper.existsByNameAndStartTimeAndEndTime(name, startTime, endTime);
    }

    @Override
    public boolean existsById(Long id) {
        return eventJPAMapper.existsById(id);
    }


}
