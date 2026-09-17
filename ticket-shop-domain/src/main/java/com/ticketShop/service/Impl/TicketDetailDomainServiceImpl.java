package com.ticketShop.service.Impl;

import com.ticketShop.model.entity.TicketDetail;
import com.ticketShop.repository.TicketDetailRepository;
import com.ticketShop.service.TicketDetailDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketDetailDomainServiceImpl implements TicketDetailDomainService {
    @Autowired
    private TicketDetailRepository ticketDetailRepository;

    @Override
    public TicketDetail getTicketDetailById(Long id) {
        return ticketDetailRepository.findById(id).orElse(null);
    }
}
