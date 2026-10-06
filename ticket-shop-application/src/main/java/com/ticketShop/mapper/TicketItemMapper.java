package com.ticketShop.mapper;

import com.ticketShop.model.command.CreateTicketItemCommand;
import com.ticketShop.model.dto.TicketItemDTO;
import com.ticketShop.model.entity.TicketItem;
import org.springframework.stereotype.Component;

@Component
public class TicketItemMapper {
    /**
     * Request → Entity
     */

    public TicketItem toEntity(CreateTicketItemCommand createItemCommand) {
        if (createItemCommand == null) {
            return null;
        }
        TicketItem ticketItem = new TicketItem();

        ticketItem.setName(createItemCommand.getName());
        ticketItem.setDescription(createItemCommand.getDescription());
        ticketItem.setStockInitial(createItemCommand.getStockInitial());
        ticketItem.setStockAvailable(createItemCommand.getStockInitial());
        ticketItem.setPriceOriginal(createItemCommand.getPriceOriginal());
        ticketItem.setPriceFlash(createItemCommand.getPriceFlash());
        ticketItem.setSaleStartTime(createItemCommand.getSaleStartTime());
        ticketItem.setSaleEndTime(createItemCommand.getSaleEndTime());

        return ticketItem;
    }


    /**
     * Entity → Response
     */
    public TicketItemDTO toDTO(TicketItem ticketItem) {

        if (ticketItem == null) {
            return null;
        }

        TicketItemDTO dto = new TicketItemDTO();

        dto.setId(ticketItem.getId());
        dto.setName(ticketItem.getName());
        dto.setDescription(ticketItem.getDescription());
        dto.setStockInitial(ticketItem.getStockInitial());
        dto.setStockAvailable(ticketItem.getStockAvailable());
        dto.setStockPrepared(ticketItem.isStockPrepared());
        dto.setPriceOriginal(ticketItem.getPriceOriginal());
        dto.setPriceFlash(ticketItem.getPriceFlash());
        dto.setSaleStartTime(ticketItem.getSaleStartTime());
        dto.setSaleEndTime(ticketItem.getSaleEndTime());
        dto.setStatus(ticketItem.getStatus());
        dto.setVersion(ticketItem.getVersion());
        dto.setEventId(ticketItem.getEventId());
        dto.setUpdatedAt(ticketItem.getUpdatedAt());
        dto.setCreatedAt(ticketItem.getCreatedAt());

        return dto;
    }
}
