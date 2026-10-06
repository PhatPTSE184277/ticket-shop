package com.ticketShop.mapper;


import com.ticketShop.dto.CreateTicketEventRequest;
import com.ticketShop.dto.CreateTicketItemRequest;
import com.ticketShop.model.command.CreateTicketEventCommand;
import com.ticketShop.model.command.CreateTicketItemCommand;
import com.ticketShop.model.entity.TicketEvent;

import java.math.BigDecimal;

/**
 * Controller Layer Mapper
 *
 * Chức năng: Chuyển đổi Request DTO (HTTP) → Command (Application)
 * Quy tắc: Chỉ mapping field + type conversion giữa API format và Application format
 */
public class TicketEventControllerMapper {
    /**
     * CreateTicketEventRequest → CreateTicketEventCommand
     */
    public static CreateTicketEventCommand toEventCommand(CreateTicketEventRequest request) {
        CreateTicketEventCommand command = new CreateTicketEventCommand();
        command.setName(request.getName());
        command.setDescription(request.getDescription());
        command.setStartTime(request.getStartTime());
        command.setEndTime(request.getEndTime());

        return command;
    }

    /**
     * CreateTicketItemRequest → CreateTicketItemCommand
     *
     * Type conversion: Long → BigDecimal (API dùng Long cho đơn giản,
     * Application dùng BigDecimal cho chính xác tài chính)
     */
    public static CreateTicketItemCommand toItemCommand(CreateTicketItemRequest request) {
        CreateTicketItemCommand command = new CreateTicketItemCommand();
        command.setName(request.getName());
        command.setDescription(request.getDescription());
        command.setStockInitial(request.getStockInitial());
        command.setStockAvailable(request.getStockAvailable());
        command.setPriceOriginal(toBigDecimal(request.getPriceOriginal()));
        command.setPriceFlash(toBigDecimal(request.getPriceFlash()));
        command.setStockPrepared(request.getStockPrepared() != null ? request.getStockPrepared() : false);

        return command;
    }

    /**
     * Helper: Long → BigDecimal (null-safe)
     */
    private static BigDecimal toBigDecimal(Long value) {
        return value != null ? BigDecimal.valueOf(value) : null;
    }
}
