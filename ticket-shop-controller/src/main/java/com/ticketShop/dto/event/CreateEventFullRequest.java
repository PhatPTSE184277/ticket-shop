package com.ticketShop.dto.event;

import com.ticketShop.dto.ticket.CreateTicketRequest;
import jakarta.validation.Valid;
import lombok.Data;

@Data
public class CreateEventFullRequest {
    @Valid
    private CreateEventRequest event;

    @Valid
    private CreateTicketRequest ticket;
}
