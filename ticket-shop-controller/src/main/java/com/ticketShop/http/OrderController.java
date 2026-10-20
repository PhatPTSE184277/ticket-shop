package com.ticketShop.http;

import com.ticketShop.service.order.OrderAppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderAppService orderAppService;

    /**
     Level 1,2
     */
    @GetMapping("/{ticketId}/{quantity}")
    public boolean orderTicketByLevel(
            @PathVariable("ticketId") Long ticketId,
            @PathVariable("quantity") int quantity
    ) {
        log.info("Controller:->orderTicketByLevel | {}, {}", ticketId, quantity);
        return orderAppService.decreaseStockLevel1(ticketId, quantity);
    }

    /**
     Level 3
     */
    @GetMapping("/{ticketId}/{quantity}/cas")
    public boolean orderTicketByLevel3(
            @PathVariable("ticketId") Long ticketId,
            @PathVariable("quantity") int quantity
    ) {
        log.info("Controller:->orderTicketByLevel3 | {}, {}", ticketId, quantity);
        return orderAppService.decreaseStockLevel3CAS(ticketId, quantity);
    }

}
