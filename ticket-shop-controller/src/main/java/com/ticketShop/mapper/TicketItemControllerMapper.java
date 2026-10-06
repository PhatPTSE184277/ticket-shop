package com.ticketShop.mapper;

import com.ticketShop.dto.CreateTicketItemRequest;
import com.ticketShop.model.command.CreateTicketItemCommand;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class TicketItemControllerMapper {
    /**
     * CreateTicketItemRequest → CreateTicketItemCommand
     *
     * Type conversion: Long → BigDecimal (API dùng Long cho đơn giản,
     * Application dùng BigDecimal cho chính xác tài chính)
     */
    public static CreateTicketItemCommand toItemCommand(CreateTicketItemRequest request) {

        if (request == null) {
            return null;
        }

        CreateTicketItemCommand command = new CreateTicketItemCommand();

        command.setEventId(request.getEventId());
        command.setName(request.getName());
        command.setDescription(request.getDescription());

        command.setStockInitial(request.getStockInitial());
        command.setStockAvailable(request.getStockAvailable());

        command.setPriceOriginal(toBigDecimal(request.getPriceOriginal()));
        command.setPriceFlash(toBigDecimal(request.getPriceFlash()));

        // Date -> LocalDateTime
        command.setSaleStartTime(toLocalDateTime(request.getSaleStartTime()));
        command.setSaleEndTime(toLocalDateTime(request.getSaleEndTime()));

        command.setStockPrepared(
                request.getStockPrepared() != null
                        ? request.getStockPrepared()
                        : false
        );

        return command;
    }


    /**
     * Helper: Long → BigDecimal (null-safe)
     */
    private static BigDecimal toBigDecimal(Long value) {
        return value != null ? BigDecimal.valueOf(value) : null;
    }

    private static LocalDateTime toLocalDateTime(Date value) {
        return value != null
                ? value.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                : null;
    }
}
