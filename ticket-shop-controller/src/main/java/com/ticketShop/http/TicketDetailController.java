package com.ticketShop.http;

import com.ticketShop.model.TicketDetailDTO;
import com.ticketShop.model.enums.ResultUtil;
import com.ticketShop.model.vo.ResultMessage;
import com.ticketShop.service.ticket.TicketDetailAppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/ticket")
@Slf4j
public class TicketDetailController {
    @Autowired
    private TicketDetailAppService  ticketDetailAppService;

    /**
     * Get ticket detail
     * @param detailId
     * @return ResultUtil
     */
    @GetMapping("/detail/{detailId}")
    @Operation(summary = "Get ticket detail by ID")
    public ResultMessage<TicketDetailDTO> getTicketDetailById(
            @Parameter(required = true)
            @PathVariable("detailId") Long detailId,

            @Parameter
            @RequestParam(name = "version", required = false) Long version
    ) {
        return ResultUtil.data(
                ticketDetailAppService.getTicketDetailById(detailId, version)
        );
    }
}
