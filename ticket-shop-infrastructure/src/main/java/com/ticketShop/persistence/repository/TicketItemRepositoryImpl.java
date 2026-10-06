package com.ticketShop.persistence.repository;

import com.ticketShop.model.entity.TicketItem;
import com.ticketShop.persistence.mapper.TicketItemJPAMapper;
import com.ticketShop.repository.TicketItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class TicketItemRepositoryImpl implements TicketItemRepository {
    @Autowired
    private TicketItemJPAMapper ticketItemJPAMapper;

    @Override
    public Optional<TicketItem> findById(Long id) {
        return ticketItemJPAMapper.findById(id);
    }

    @Override
    public List<TicketItem> findByEventId(Long eventId) {
        return ticketItemJPAMapper.findByEventId(eventId);
    }

    @Override
    public TicketItem save(TicketItem ticketItem) {
        log.info("Saving TicketItem: {}", ticketItem.getName());
        return ticketItemJPAMapper.save(ticketItem);
    }


}
