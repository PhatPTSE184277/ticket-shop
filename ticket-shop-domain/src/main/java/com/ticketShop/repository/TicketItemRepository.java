package com.ticketShop.repository;

import com.ticketShop.model.entity.TicketItem;

import java.util.List;
import java.util.Optional;

public interface TicketItemRepository {
    /**
     * Tìm TicketItem theo ID
     * @param id
     * @return Optional chứa TicketItem
     */
    Optional<TicketItem> findById(Long id);


    /**
     * Tìm tất cả TicketDetail theo eventId (ticketId)
     * @param eventId
     * @return List TicketItem
     */
    List<TicketItem> findByEventId(Long eventId);

    /**
     * Cập nhật status của tất cả TicketItem theo eventId
     * @param eventId
     * @param status
     */
    void updateStatusByEventId(Long eventId, int status);

    /**
     * Lưu TicketItem mới
     * @param ticketItem
     * @return TicketDetail được lưu
     */
    TicketItem save(TicketItem ticketItem);


    //Check
    boolean existsByEventIdAndName(Long eventId, String name);
}
