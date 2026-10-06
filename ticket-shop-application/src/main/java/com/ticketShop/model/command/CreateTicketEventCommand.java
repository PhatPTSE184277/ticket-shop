package com.ticketShop.model.command;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateTicketEventCommand {

    private String name;

    private String description;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
