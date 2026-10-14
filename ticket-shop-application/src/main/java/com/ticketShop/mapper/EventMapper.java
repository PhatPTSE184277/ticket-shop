package com.ticketShop.mapper;

import com.ticketShop.model.command.CreateEventCommand;
import com.ticketShop.model.dto.EventDTO;
import com.ticketShop.model.entity.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public Event toEntity(CreateEventCommand createEventCommand) {
        if (createEventCommand == null) {
            return null;
        }

        Event event = new Event();

        event.setName(createEventCommand.getName());
        event.setDescription(createEventCommand.getDescription());
        event.setStartTime(createEventCommand.getStartTime());
        event.setEndTime(createEventCommand.getEndTime());

        return event;
    }

    public EventDTO toDTO(Event event) {
        if (event == null) {
            return null;
        }

        EventDTO dto = new EventDTO();

        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setDescription(event.getDescription());
        dto.setStartTime(event.getStartTime());
        dto.setEndTime(event.getEndTime());
        dto.setStatus(event.getStatus());
        dto.setUpdatedAt(event.getUpdatedAt());
        dto.setCreatedAt(event.getCreatedAt());

        return dto;
    }
}