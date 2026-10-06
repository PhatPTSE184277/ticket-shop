package com.ticketShop.mapper;

import com.ticketShop.model.command.CreateTicketEventCommand;
import com.ticketShop.model.dto.TicketEventDTO;
import com.ticketShop.model.entity.TicketEvent;
import org.springframework.stereotype.Component;

@Component
public class TicketEventMapper {

    public TicketEvent toEntity(CreateTicketEventCommand createEventCommand) {
        if (createEventCommand == null) {
            return null;
        }

        TicketEvent ticketEvent = new TicketEvent();

        ticketEvent.setName(createEventCommand.getName());
        ticketEvent.setDescription(createEventCommand.getDescription());
        ticketEvent.setStartTime(createEventCommand.getStartTime());
        ticketEvent.setEndTime(createEventCommand.getEndTime());

        return ticketEvent;
    }

    public TicketEventDTO toDTO(TicketEvent ticketEvent) {
        if (ticketEvent == null) {
            return null;
        }

        TicketEventDTO dto = new TicketEventDTO();

        dto.setId(ticketEvent.getId());
        dto.setName(ticketEvent.getName());
        dto.setDescription(ticketEvent.getDescription());
        dto.setStartTime(ticketEvent.getStartTime());
        dto.setEndTime(ticketEvent.getEndTime());
        dto.setStatus(ticketEvent.getStatus());
        dto.setUpdatedAt(ticketEvent.getUpdatedAt());
        dto.setCreatedAt(ticketEvent.getCreatedAt());

        return dto;
    }
}