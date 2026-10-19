package com.ticketShop.service.Impl;

import com.ticketShop.repository.OrderRepository;
import com.ticketShop.service.OrderDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDomainServiceImpl implements OrderDomainService {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public boolean decreaseStockLevel1(Long tickerId, int quantity) {
        return orderRepository.decreaseStockLevel1(tickerId, quantity);
    }

    @Override
    public boolean decreaseStockLevel2(Long tickerId, int quantity) {
        return false;
    }

    @Override
    public boolean decreaseStockLevel3CAS(Long tickerId, int oldStockAvailable, int quantity) {
        return orderRepository.decreaseStockLevel3CAS(tickerId, oldStockAvailable, quantity);
    }

    @Override
    public int getStockAvailable(Long ticketId) {
        return orderRepository.getStockAvailable(ticketId);
    }

    @Override
    public boolean increaseStock(Long tickerId, int quantity) {
        return orderRepository.increaseStock(tickerId, quantity);
    }
}
