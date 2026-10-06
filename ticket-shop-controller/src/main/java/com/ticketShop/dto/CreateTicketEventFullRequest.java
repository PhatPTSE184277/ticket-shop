package com.ticketShop.dto;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class CreateTicketEventFullRequest {
    @Valid
    private CreateTicketEventRequest ticketEvent;

    @Valid
    private CreateTicketItemRequest ticketItem;
}
