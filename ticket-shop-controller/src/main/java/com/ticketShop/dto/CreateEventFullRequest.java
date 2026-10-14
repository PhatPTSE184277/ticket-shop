package com.ticketShop.dto;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class CreateEventFullRequest {
    @Valid
    private CreateEventRequest event;

    @Valid
    private CreateTicketRequest ticket;
}
