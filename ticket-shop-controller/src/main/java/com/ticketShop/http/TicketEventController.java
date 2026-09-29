package com.ticketShop.http;

import com.ticketShop.model.dto.response.TicketEventResponse;
import com.ticketShop.model.enums.ResultUtil;
import com.ticketShop.model.vo.ResultMessage;
import com.ticketShop.service.ticket.TicketEventAppService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResultMessage<List<TicketEventResponse>> getAllActiveTicketEvents() {

        return ResultUtil.data(
                ticketEventAppService.getAllActiveTicketEvents()
        );
    }
}