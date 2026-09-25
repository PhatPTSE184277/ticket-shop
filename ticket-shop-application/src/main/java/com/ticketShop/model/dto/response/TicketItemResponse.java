package com.ticketShop.model.dto.response;

import com.ticketShop.model.enums.TicketItemStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TicketItemResponse {

    private Long id;

    private String name;

    private String description;

    private int stockInitial;

    private int stockAvailable;

    private boolean stockPrepared;

    private BigDecimal priceOriginal;

    private BigDecimal priceFlash;

    private LocalDateTime saleStartTime;

    private LocalDateTime saleEndTime;

    private TicketItemStatus status;

    private Long version;

    private Long eventId;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;
}