package com.ticketShop.persistence.mapper;

import com.ticketShop.model.entity.TicketEvent;
import com.ticketShop.model.enums.TicketEventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketEventJPAMapper extends JpaRepository<TicketEvent, Long> {
    List<TicketEvent> findByStatus(TicketEventStatus status);;


    //check
    boolean existsByNameAndStartTimeAndEndTime(String name, LocalDateTime startTime, LocalDateTime endTime);
    boolean existsById(Long id);
}
