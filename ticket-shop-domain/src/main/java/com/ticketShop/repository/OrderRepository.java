package com.ticketShop.repository;

public interface OrderRepository {
    boolean decreaseStockLevel1(Long tickerId, int quantity);
    boolean decreaseStockLevel2(Long tickerId, int quantity);
    boolean decreaseStockLevel3CAS(Long tickerId, int oldStockAvailable, int quantity);

    int getStockAvailable(Long tickerId);

    /**
     * Thực hiện câu lệnh SQL hoàn kho vào Database
     */
    boolean increaseStock(Long tickerId, int quantity);
}
