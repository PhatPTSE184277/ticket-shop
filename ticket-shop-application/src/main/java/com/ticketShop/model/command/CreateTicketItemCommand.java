package com.ticketShop.model.command;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateTicketItemCommand {

    private String name;

    private String description;

    private int stockInitial;

    private int stockAvailable;

    private boolean stockPrepared;

    private BigDecimal priceOriginal;

    private BigDecimal priceFlash;

    private LocalDateTime saleStartTime;

    private LocalDateTime saleEndTime;

    private Long eventId;
}