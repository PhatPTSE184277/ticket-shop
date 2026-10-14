package com.ticketShop.mapper;

import com.ticketShop.model.command.CreateTicketCommand;
import com.ticketShop.model.dto.TicketDTO;
import com.ticketShop.model.entity.Ticket;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {
    /**
     * Request → Entity
     */

    public Ticket toEntity(CreateTicketCommand createTicketCommand) {
        if (createTicketCommand == null) {
            return null;
        }
        Ticket ticket = new Ticket();

        ticket.setName(createTicketCommand.getName());
        ticket.setDescription(createTicketCommand.getDescription());
        ticket.setStockInitial(createTicketCommand.getStockInitial());
        ticket.setStockAvailable(createTicketCommand.getStockInitial());
        ticket.setPriceOriginal(createTicketCommand.getPriceOriginal());
        ticket.setPriceFlash(createTicketCommand.getPriceFlash());
        ticket.setSaleStartTime(createTicketCommand.getSaleStartTime());
        ticket.setSaleEndTime(createTicketCommand.getSaleEndTime());
        ticket.setEventId(createTicketCommand.getEventId());

        return ticket;
    }


    /**
     * Entity → Response
     */
    public TicketDTO toDTO(Ticket ticket) {

        if (ticket == null) {
            return null;
        }

        TicketDTO dto = new TicketDTO();

        dto.setId(ticket.getId());
        dto.setName(ticket.getName());
        dto.setDescription(ticket.getDescription());
        dto.setStockInitial(ticket.getStockInitial());
        dto.setStockAvailable(ticket.getStockAvailable());
        dto.setStockPrepared(ticket.isStockPrepared());
        dto.setPriceOriginal(ticket.getPriceOriginal());
        dto.setPriceFlash(ticket.getPriceFlash());
        dto.setSaleStartTime(ticket.getSaleStartTime());
        dto.setSaleEndTime(ticket.getSaleEndTime());
        dto.setStatus(ticket.getStatus());
        dto.setVersion(ticket.getVersion());
        dto.setEventId(ticket.getEventId());
        dto.setUpdatedAt(ticket.getUpdatedAt());
        dto.setCreatedAt(ticket.getCreatedAt());

        return dto;
    }
}
