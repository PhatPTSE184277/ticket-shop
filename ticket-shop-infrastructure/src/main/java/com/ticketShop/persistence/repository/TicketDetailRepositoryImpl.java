package com.ticketShop.persistence.repository;

import com.ticketShop.model.entity.TicketDetail;
import com.ticketShop.persistence.mapper.TicketDetailJPAMapper;
import com.ticketShop.repository.TicketDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TicketDetailRepositoryImpl implements TicketDetailRepository {
    @Autowired
    private TicketDetailJPAMapper ticketDetailJPAMapper;

    @Override
    public Optional<TicketDetail> findById(Long id) {
        return ticketDetailJPAMapper.findById(id);
    }
}
