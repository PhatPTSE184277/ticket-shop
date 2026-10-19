package com.ticketShop.persistence.repository;

import com.ticketShop.persistence.mapper.OrderJPAMapper;
import com.ticketShop.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderRepositoryImpl implements OrderRepository {
    @Autowired
    private OrderJPAMapper orderJPAMapper;

    @Override
    public boolean decreaseStockLevel1(Long tickerId, int quantity) {
        log.info("Run test:decreaseStockLevel1 with: | {}, {} ", tickerId, quantity);
        return orderJPAMapper.decreaseStockLevel1(tickerId, quantity) > 0;
    }

    @Override
    public boolean decreaseStockLevel2(Long tickerId, int quantity) {
        return false;
    }

    @Override
    public boolean decreaseStockLevel3CAS(Long tickerId, int oldStockAvailable, int quantity) {
        log.info("Run test:decreaseStockLevel3CAS with: | {}, {}, {} ", tickerId, oldStockAvailable, quantity);
        return orderJPAMapper.decreaseStockLevel3CAS(tickerId, oldStockAvailable, quantity) > 0;
    }

    @Override
    public int getStockAvailable(Long tickerId) {
        return orderJPAMapper.getStockAvailable(tickerId);
    }

    @Override
    public boolean increaseStock(Long tickerId, int quantity) {
        log.info("Rollback stock: increaseStock for ticketId: {} | quantity: {}", tickerId, quantity);
        // Gọi method increaseStock mà bạn đã thêm vào TicketOrderJPAMapper (Bước 2.1 ở tin nhắn trước)
        return orderJPAMapper.increaseStock(tickerId, quantity) > 0;
    }
}
