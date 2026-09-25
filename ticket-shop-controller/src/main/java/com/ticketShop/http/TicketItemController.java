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
    public ResultMessage<TicketItemResponse> getTicketDetailById(
            @Parameter(required = true)
            @PathVariable("ticketId") Long ticketId,

            @Parameter
            @RequestParam(name = "version", required = false) Long version
    ) {
        return ResultUtil.data(
                ticketItemAppService.getTicketItemById(ticketId, version)
        );
    }
}
