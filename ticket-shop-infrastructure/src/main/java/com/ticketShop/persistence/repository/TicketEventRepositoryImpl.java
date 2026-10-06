package com.ticketShop.persistence.repository;

import com.ticketShop.model.entity.TicketEvent;
import com.ticketShop.model.enums.TicketEventStatus;
import com.ticketShop.persistence.mapper.TicketEventJPAMapper;
import com.ticketShop.repository.TicketEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Slf4j
public class TicketEventRepositoryImpl implements TicketEventRepository {
    @Autowired
    private TicketEventJPAMapper ticketEventJPAMapper;

    @Override
    public List<TicketEvent> findAllActive() {
        return ticketEventJPAMapper.findByStatus(TicketEventStatus.ACTIVE);
    }

    @Override
    public TicketEvent save(TicketEvent ticketEvent) {
        log.info("Saving ticket Event: {}", ticketEvent.getName());
        return ticketEventJPAMapper.save(ticketEvent);
    }

    @Override
    public boolean existsByNameAndStartTimeAndEndTime(String name, LocalDateTime startTime, LocalDateTime endTime) {
        return ticketEventJPAMapper.existsByNameAndStartTimeAndEndTime(name, startTime, endTime);
    }


}
