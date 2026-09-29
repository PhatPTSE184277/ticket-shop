package com.ticketShop.http;

import com.ticketShop.model.dto.response.TicketItemResponse;
import com.ticketShop.model.enums.ResultUtil;
import com.ticketShop.model.vo.ResultMessage;
import com.ticketShop.service.ticket.TicketItemAppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
    @RequestMapping("/ticket")
@Slf4j
public class TicketItemController {
    @Autowired
    private TicketItemAppService ticketItemAppService;

    /**
     * Get ticket item
     * @param ticketId
     * @return ResultUtil
     */
    @GetMapping("/{ticketId}")
    @Operation(summary = "Get ticket item by ID")
    public ResultMessage<TicketItemResponse> getTicketItemById(
            @Parameter(required = true)
            @PathVariable("ticketId") Long ticketId,

            @Parameter
            @RequestParam(name = "version", required = false) Long version
    ) {
        return ResultUtil.data(
                ticketItemAppService.getTicketItemById(ticketId, version)
        );
    }

    /**
     * Get all ticket items by event ID
     *
     * GET /api/ticket/event/{eventId}/items
     */
    @GetMapping("/event/{eventId}/items")
    @Operation(summary = "Get ticket items by event ID")
    public ResultMessage<List<TicketItemResponse>> getTicketItemsByEventId(

            @Parameter(required = true)
            @PathVariable("eventId")
            Long eventId
    ) {
        return ResultUtil.data(
                ticketItemAppService.getTicketItemsByEventId(eventId)
        );
    }
}
