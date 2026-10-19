package com.ticketShop.service.ticket;

public interface OrderAppService {
    boolean decreaseStockLevel1(Long tickerId, int quantity);
    boolean decreaseStockLevel2(Long tickerId, int quantity);
    boolean decreaseStockLevel3CAS(Long tickerId, int quantity);

    int getStockAvailable(Long ticketId);

}
