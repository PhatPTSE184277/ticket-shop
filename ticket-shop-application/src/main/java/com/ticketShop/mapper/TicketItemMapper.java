package com.ticketShop.mapper;

import com.ticketShop.model.dto.response.TicketItemResponse;
import com.ticketShop.model.entity.TicketItem;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class TicketItemMapper {

    /**
     * Entity → Response
     */
    public TicketItemResponse toResponse(TicketItem ticketItem) {

        if (ticketItem == null) {
            return null;
        }

        TicketItemResponse response = new TicketItemResponse();

        response.setId(ticketItem.getId());
        response.setName(ticketItem.getName());
        response.setDescription(ticketItem.getDescription());
        response.setStockInitial(ticketItem.getStockInitial());
        response.setStockAvailable(ticketItem.getStockAvailable());
        response.setStockPrepared(ticketItem.isStockPrepared());
        response.setPriceOriginal(ticketItem.getPriceOriginal());
        response.setPriceFlash(ticketItem.getPriceFlash());
        response.setSaleStartTime(ticketItem.getSaleStartTime());
        response.setSaleEndTime(ticketItem.getSaleEndTime());
        response.setStatus(ticketItem.getStatus());
        response.setVersion(ticketItem.getVersion());
        response.setEventId(ticketItem.getEventId());
        response.setUpdatedAt(ticketItem.getUpdatedAt());
        response.setCreatedAt(ticketItem.getCreatedAt());

        return response;
    }
}
