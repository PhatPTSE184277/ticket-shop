package com.xxxx.ddd.controller.http;

import com.xxxx.ddd.application.model.TicketDetailDTO;
import com.xxxx.ddd.application.service.ticket.TicketDetailAppService;
import com.xxxx.ddd.controller.model.enums.ResultUtil;
import com.xxxx.ddd.controller.model.vo.ResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket")
@Slf4j
public class TicketDetailController {
    // CALL Service Application
    @Autowired
    private TicketDetailAppService ticketDetailAppService;

    /**
     * Get ticket detail
     * @param ticketId
     * @param detailId
     * @return ResultUtil
     */
    @GetMapping("/{ticketId}/detail/{detailId}")
    public ResultMessage<TicketDetailDTO> getTicketDetail(
            @PathVariable("ticketId") long ticketId,
            @PathVariable("detailId") long detailId,
            @RequestParam(name = "version", required = false) Long version
    ){
        return ResultUtil.data(ticketDetailAppService.getTicketDetailById(detailId, version));
    }

    @GetMapping("/{ticketId}/detail/{detailId}/order")
    public boolean orderTicketByUser(
            @PathVariable("ticketId") long ticketId,
            @PathVariable("detailId") long detailId
    ){
        return ticketDetailAppService.orderTicketDetail(detailId);
    }
}
