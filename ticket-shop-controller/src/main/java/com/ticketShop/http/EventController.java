package com.ticketShop.http;

import com.ticketShop.dto.event.CreateEventFullRequest;
import com.ticketShop.mapper.EventControllerMapper;
import com.ticketShop.model.command.CreateEventCommand;
import com.ticketShop.model.command.CreateTicketCommand;
import com.ticketShop.model.dto.EventDTO;
import com.ticketShop.model.enums.ResultUtil;
import com.ticketShop.model.vo.ResultMessage;
import com.ticketShop.service.event.EventAppService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
@Slf4j
public class EventController {

    @Autowired
    private EventAppService eventAppService;

    /**
     * Get all active events
     */
    @GetMapping("/active")
    @Operation(summary = "Get all active events")
    public ResultMessage<List<EventDTO>> getAllActiveEvents() {

        return ResultUtil.data(
                eventAppService.getAllActiveEvents()
        );
    }

    /**
     * Tạo event mới
     *
     * POST /event/create
     *
     * Request Body:
     {
     "Event": {
     "name": "Concert ABC",
     "description": "Concert held in HCM City",
     "startTime": "2024-05-01 18:00:00",
     "endTime": "2024-05-01 22:00:00"
     },
     "Ticket": {
     "name": "VIP",
     "stockInitial": 100,
     "stockAvailable": 100,
     "priceOriginal": 500000
     }
     }
     *
     * @param request
     * @return ResultMessage<EventResponse>
     */
    @PostMapping("/create")
    public ResultMessage<EventDTO> createEvent(@Valid @RequestBody CreateEventFullRequest request) {
        log.info("Creating event: {}", request.getEvent().getName());

        // Map request -> command
        CreateEventCommand eventCommand = EventControllerMapper.toEventCommand(request.getEvent());
        CreateTicketCommand ticketCommand = EventControllerMapper.toTicketCommand(request.getTicket());

        // Gọi service xử lý nghiệp vụ
        EventDTO eventDTO = eventAppService.createEvent(eventCommand, ticketCommand);

        return ResultUtil.data(eventDTO);
    }
}