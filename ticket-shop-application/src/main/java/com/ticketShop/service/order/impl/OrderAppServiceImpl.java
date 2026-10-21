package com.ticketShop.service.order.impl;

import com.ticketShop.distributed.redisson.RedisDistributedService;
import com.ticketShop.service.OrderDomainService;
import com.ticketShop.service.order.OrderAppService;
import com.ticketShop.service.order.cache.StockOrderCacheService;
import jakarta.persistence.LockTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PessimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
public class OrderAppServiceImpl implements OrderAppService {

    private static final AtomicLong ORDER_SEQ = new AtomicLong(0);

    @Autowired
    private OrderDomainService orderDomainService;

    @Autowired
    private StockOrderCacheService  stockOrderCacheService;

    @Autowired
    private RedisDistributedService redisDistributedService;

    // SELECT
    // UPDATE
    @Override
    public boolean decreaseStockLevel1(Long tickerId, int quantity) {
     try {
         int stockAvailable = orderDomainService.getStockAvailable(tickerId);// LOCK MYSQL -> SELECT ... FOR UPDATE;
         if (stockAvailable < quantity) {
             log.info("Case: stockAvailable < quantity | {}, {}", stockAvailable, quantity);
             return false;
         }
         return orderDomainService.decreaseStockLevel1(tickerId, quantity);
     } catch (PessimisticLockException e) {
         log.warn("Pessimistic Locking failed for ticketId={}", tickerId);
         return false;
     } catch (LockTimeoutException e) {
         log.error("Lock timeout while processing ticketId={}", tickerId, e);
         return false;
     } catch (Exception e) {
         log.error("Unexpected error when decreasing stock for ticketId={}", tickerId, e);
         return false;
     }
    }

    // UPDATE product SET stock = stock - quantity WHERE productId = 1 AND stock > 0; > quantity -> LOCK row -> innodb MYSQL
    // version -> Lock thread
    // UPDATE product SET stock = stock - quantity, version = version + 1 WHERE productId = 1 AND stock > 0 AND version = 10;

    @Override
    public boolean decreaseStockLevel2(Long tickerId, int quantity) {
        return false;
    }

    @Override
    public boolean decreaseStockLevel3CAS(Long tickerId, int quantity) {
        try {
            int stockAvailable = orderDomainService.getStockAvailable(tickerId);
            if (stockAvailable < quantity) {
                return false;
            }
            orderDomainService.decreaseStockLevel3CAS(tickerId, stockAvailable,quantity);
            return false;
        } catch (PessimisticLockException e) {
            log.warn("Pessimistic Locking failed for ticketId={}", tickerId);
            return false;
        } catch (LockTimeoutException e) {
            log.error("Lock timeout while processing ticketId={}", tickerId, e);
            return false;
        } catch (Exception e) {
            log.error("Unexpected error when decreasing stock for ticketId={}", tickerId, e);
            return false;
        }
    }

    @Override
    public int getStockAvailable(Long ticketId) {
        return 0;
    }
}
