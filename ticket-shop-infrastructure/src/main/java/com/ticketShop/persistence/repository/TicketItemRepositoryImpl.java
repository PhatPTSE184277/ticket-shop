package com.ticketShop.persistence.repository;

import com.ticketShop.model.entity.TicketItem;
import com.ticketShop.persistence.mapper.TicketItemJPAMapper;
import com.ticketShop.repository.TicketItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TicketItemRepositoryImpl implements TicketItemRepository {
    @Autowired
    private TicketItemJPAMapper ticketItemJPAMapper;

    @Override
    public Optional<TicketItem> findById(Long id) {
        return ticketItemJPAMapper.findById(id);
    }

}
