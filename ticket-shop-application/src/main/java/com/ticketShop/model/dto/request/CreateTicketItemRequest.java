package com.ticketShop.model.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateTicketItemRequest {

    private String name;

    private String description;

    private int stockInitial;

    private boolean stockPrepared;

    private BigDecimal priceOriginal;

    private BigDecimal priceFlash;

    private LocalDateTime saleStartTime;

    private LocalDateTime saleEndTime;

    private Long eventId;
}