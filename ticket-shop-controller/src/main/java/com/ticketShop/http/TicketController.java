package com.ticketShop.http;

import com.ticketShop.dto.ticket.CreateTicketRequest;
import com.ticketShop.mapper.TicketControllerMapper;
import com.ticketShop.model.command.CreateTicketCommand;
import com.ticketShop.model.dto.TicketDTO;
import com.ticketShop.model.enums.ResultUtil;
import com.ticketShop.model.vo.ResultMessage;
import com.ticketShop.service.ticket.TicketAppService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
    @RequestMapping("/ticket")
@Slf4j
public class TicketController {
    @Autowired
    private TicketAppService ticketAppService;

    /**
     * Get ticket
     * @param ticketId
     * @return ResultUtil
     */
    @GetMapping("/{ticketId}")
    @Operation(summary = "Get ticket by ID")
    public ResultMessage<TicketDTO> getTicketById(
            @Parameter(required = true)
            @PathVariable("ticketId") Long ticketId,

            @Parameter
            @RequestParam(name = "version", required = false) Long version
    ) {
        return ResultUtil.data(
                ticketAppService.getTicketById(ticketId, version)
        );
    }

    /**
     * Get all tickets by event ID
     *
     * GET /api/ticket/event/{eventId}
     */
    @GetMapping("/event/{eventId}")
    @Operation(summary = "Get tickets by event ID")
    public ResultMessage<List<TicketDTO>> getTicketsByEventId(

            @Parameter(required = true)
            @PathVariable("eventId")
            Long eventId
    ) {
        return ResultUtil.data(
                ticketAppService.getTicketsByEventId(eventId)
        );
    }

    @PostMapping("/create")
    public ResultMessage<TicketDTO> createTicket(@Valid @RequestBody CreateTicketRequest request){
        log.info("Creating ticket: {}", request.getName());

        // Map request -> command
        CreateTicketCommand ticketCommand = TicketControllerMapper.toTicketCommand(request);

        // Gọi service xử lý nghiệp vụ
        TicketDTO ticketDTO = ticketAppService.createTicket(ticketCommand);

        return ResultUtil.data(ticketDTO);
    }
}
