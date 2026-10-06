package com.ticketShop.http;

import com.ticketShop.dto.CreateTicketEventFullRequest;
import com.ticketShop.mapper.TicketEventControllerMapper;
import com.ticketShop.mapper.TicketEventMapper;
import com.ticketShop.model.command.CreateTicketEventCommand;
import com.ticketShop.model.command.CreateTicketItemCommand;
import com.ticketShop.model.dto.TicketEventDTO;
import com.ticketShop.model.enums.ResultUtil;
import com.ticketShop.model.vo.ResultMessage;
import com.ticketShop.service.ticket.TicketEventAppService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticket/event")
@Slf4j
public class TicketEventController {

    @Autowired
    private TicketEventAppService ticketEventAppService;

    /**
     * Get all active ticket events
     */
    @GetMapping("/active")
    @Operation(summary = "Get all active ticket events")
    public ResultMessage<List<TicketEventDTO>> getAllActiveTicketEvents() {

        return ResultUtil.data(
                ticketEventAppService.getAllActiveTicketEvents()
        );
    }

    /**
     * Tạo ticket event mới
     *
     * POST /ticket/event/create
     *
     * Request Body:
     {
     "ticketEvent": {
     "name": "Concert ABC",
     "description": "Concert held in HCM City",
     "startTime": "2024-05-01 18:00:00",
     "endTime": "2024-05-01 22:00:00"
     },
     "ticketItem": {
     "name": "VIP",
     "stockInitial": 100,
     "stockAvailable": 100,
     "priceOriginal": 500000
     }
     }
     *
     * @param request
     * @return ResultMessage<TicketEventResponse>
     */
    @PostMapping("/create")
    public ResultMessage<TicketEventDTO> createTicket(@Valid @RequestBody CreateTicketEventFullRequest request) {
        log.info("Creating ticket event: {}", request.getTicketEvent().getName());

        // Map request -> command
        CreateTicketEventCommand ticketEventCommand = TicketEventControllerMapper.toEventCommand(request.getTicketEvent());
        CreateTicketItemCommand ticketItemCommand = TicketEventControllerMapper.toItemCommand(request.getTicketItem());

        // Gọi service xử lý nghiệp vụ
        TicketEventDTO ticketEventDTO = ticketEventAppService.createTicketEvent(ticketEventCommand, ticketItemCommand);

        return ResultUtil.data(ticketEventDTO);
    }
}