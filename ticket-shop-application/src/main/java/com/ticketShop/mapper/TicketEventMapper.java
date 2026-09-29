package com.ticketShop.mapper;

import com.ticketShop.model.dto.response.TicketEventResponse;
import com.ticketShop.model.entity.TicketEvent;
import org.springframework.stereotype.Component;

@Component
public class TicketEventMapper {

    public TicketEventResponse toResponse(TicketEvent ticketEvent) {
        if (ticketEvent == null) {
            return null;
        }

        TicketEventResponse response = new TicketEventResponse();

        response.setId(ticketEvent.getId());
        response.setName(ticketEvent.getName());
        response.setDescription(ticketEvent.getDescription());
        response.setStartTime(ticketEvent.getStartTime());
        response.setEndTime(ticketEvent.getEndTime());
        response.setStatus(ticketEvent.getStatus());
        response.setUpdatedAt(ticketEvent.getUpdatedAt());
        response.setCreatedAt(ticketEvent.getCreatedAt());

        return response;
    }
}