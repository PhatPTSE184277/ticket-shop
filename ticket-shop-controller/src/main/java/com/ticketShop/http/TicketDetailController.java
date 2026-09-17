package com.ticketShop.http;

import com.ticketShop.model.TicketDetailDTO;
import com.ticketShop.model.enums.ResultUtil;
import com.ticketShop.model.vo.ResultMessage;
import com.ticketShop.service.ticket.TicketDetailAppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket")
@Slf4j
public class TicketDetailController {
    @Autowired
    private TicketDetailAppService  ticketDetailAppService;

    /**
     * Get ticket detail
     * @param ticketId
     * @return ResultUtil
     */
    @GetMapping("/detail/{ticketId}")
    public ResultMessage<TicketDetailDTO> getTicketDetailById(
            @PathVariable Long ticketId,
            @RequestParam   (name = "version", required = false) Long version) {
        return ResultUtil.data(ticketDetailAppService.getTicketDetailById(ticketId, version));
    }
}
