package com.ticketShop.mapper;

import com.ticketShop.model.TicketDetailDTO;
import com.ticketShop.model.entity.TicketDetail;
import org.springframework.beans.BeanUtils;

public class TicketDetailMapper {
    public static TicketDetailDTO mapperTOTicketDetailDTO(TicketDetail ticketDetail) {
        if (ticketDetail == null) {
            return null;
        }

        TicketDetailDTO ticketDetailDTO = new TicketDetailDTO();
        BeanUtils.copyProperties(ticketDetail, ticketDetailDTO);
        return ticketDetailDTO;
    }
}
