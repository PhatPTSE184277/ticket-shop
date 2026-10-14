package com.ticketShop.repository;

import com.ticketShop.model.entity.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketRepository {
    /**
     * Tìm Ticket theo ID
     * @param id
     * @return Optional chứa Ticket
     */
    Optional<Ticket> findById(Long id);


    /**
     * Tìm tất cả Ticket theo eventId (ticketId)
     * @param eventId
     * @return List Ticket
     */
    List<Ticket> findByEventId(Long eventId);

    /**
     * Cập nhật status của tất cả Ticket theo eventId
     * @param eventId
     * @param status
     */
    void updateStatusByEventId(Long eventId, int status);

    /**
     * Lưu Ticket mới
     * @param ticket
     * @return Ticket được lưu
     */
    Ticket save(Ticket ticket);


    //Check
    boolean existsByEventIdAndName(Long eventId, String name);
}
