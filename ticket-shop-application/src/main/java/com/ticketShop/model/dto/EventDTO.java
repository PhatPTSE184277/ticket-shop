package com.ticketShop.model.dto;

import com.ticketShop.model.enums.EventStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventDTO {

    private Long id;

    private String name;

    private String description;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private EventStatus status;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;
}