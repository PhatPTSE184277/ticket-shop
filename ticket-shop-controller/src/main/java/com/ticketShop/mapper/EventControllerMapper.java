package com.ticketShop.mapper;


import com.ticketShop.dto.CreateEventRequest;
import com.ticketShop.dto.CreateTicketRequest;
import com.ticketShop.model.command.CreateEventCommand;
import com.ticketShop.model.command.CreateTicketCommand;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Controller Layer Mapper
 *
 * Chức năng: Chuyển đổi Request DTO (HTTP) → Command (Application)
 * Quy tắc: Chỉ mapping field + type conversion giữa API format và Application format
 */
public class EventControllerMapper {
    /**
     * CreateEventRequest → CreateEventCommand
     */
    public static CreateEventCommand toEventCommand(CreateEventRequest request) {
        CreateEventCommand command = new CreateEventCommand();
        command.setName(request.getName());
        command.setDescription(request.getDescription());
        command.setStartTime(request.getStartTime());
        command.setEndTime(request.getEndTime());

        return command;
    }

    /**
     * CreateTicketRequest → CreateTicketCommand
     *
     * Type conversion: Long → BigDecimal (API dùng Long cho đơn giản,
     * Application dùng BigDecimal cho chính xác tài chính)
     */
    public static CreateTicketCommand toTicketCommand(CreateTicketRequest request) {
        CreateTicketCommand command = new CreateTicketCommand();
        command.setName(request.getName());
        command.setDescription(request.getDescription());
        command.setStockInitial(request.getStockInitial());
        command.setStockAvailable(request.getStockAvailable());
        command.setPriceOriginal(toBigDecimal(request.getPriceOriginal()));
        command.setPriceFlash(toBigDecimal(request.getPriceFlash()));
        // Date -> LocalDateTime
        command.setSaleStartTime(toLocalDateTime(request.getSaleStartTime()));
        command.setSaleEndTime(toLocalDateTime(request.getSaleEndTime()));
        command.setStockPrepared(request.getStockPrepared() != null ? request.getStockPrepared() : false);

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
