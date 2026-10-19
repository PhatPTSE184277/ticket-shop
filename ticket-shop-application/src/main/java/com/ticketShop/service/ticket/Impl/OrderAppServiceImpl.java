package com.ticketShop.service.ticket.Impl;

import com.ticketShop.service.OrderDomainService;
import com.ticketShop.service.ticket.OrderAppService;
import jakarta.persistence.LockTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PessimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderAppServiceImpl implements OrderAppService {
    @Autowired
    private OrderDomainService orderDomainService;

    // SELECT
    // UPDATE
    @Override
    public boolean decreaseStockLevel1(Long tickerId, int quantity) {
     try {
         int stockAvailable = orderDomainService.getStockAvailable(tickerId);
         if (stockAvailable < quantity) {
             return false;
         }
         orderDomainService.decreaseStockLevel1(tickerId, quantity);
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
