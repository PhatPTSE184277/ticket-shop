package com.ticketShop.model.dto;

import com.ticketShop.model.enums.TicketEventStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketEventDTO {

    private Long id;

    private String name;

    private String description;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private TicketEventStatus status;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;
}