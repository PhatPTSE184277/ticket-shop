package com.ticketShop.http;

import com.ticketShop.dto.CreateTicketItemRequest;
import com.ticketShop.mapper.TicketItemControllerMapper;
import com.ticketShop.model.command.CreateTicketItemCommand;
import com.ticketShop.model.dto.TicketItemDTO;
import com.ticketShop.model.enums.ResultUtil;
import com.ticketShop.model.vo.ResultMessage;
import com.ticketShop.service.ticket.TicketItemAppService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
    @RequestMapping("/ticket/item")
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
    public ResultMessage<TicketItemDTO> getTicketItemById(
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
    public ResultMessage<List<TicketItemDTO>> getTicketItemsByEventId(

            @Parameter(required = true)
            @PathVariable("eventId")
            Long eventId
    ) {
        return ResultUtil.data(
                ticketItemAppService.getTicketItemsByEventId(eventId)
        );
    }

    @PostMapping("/create")
    public ResultMessage<TicketItemDTO> createTicketItem(@Valid @RequestBody CreateTicketItemRequest request){
        log.info("Creating ticket item: {}", request.getName());

        // Map request -> command
        CreateTicketItemCommand ticketItemCommand = TicketItemControllerMapper.toItemCommand(request);

        // Gọi service xử lý nghiệp vụ
        TicketItemDTO ticketItemDTO = ticketItemAppService.createTicketItem(ticketItemCommand);

        return ResultUtil.data(ticketItemDTO);
    }
}
