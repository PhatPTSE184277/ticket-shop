package com.ticketShop.mapper;

import com.ticketShop.model.dto.response.TicketItemResponse;
import com.ticketShop.model.entity.TicketItem;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class TicketItemMapper {

    public TicketItemResponse toResponse(TicketItem ticketItem) {

        if (ticketItem == null) {
            return null;
        }

        TicketItemResponse response = new TicketItemResponse();

        BeanUtils.copyProperties(ticketItem, response);

        return response;
    }
}
