package com.ticketShop.validator;

import com.ticketShop.model.entity.TicketEvent;
import com.ticketShop.repository.TicketEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TicketEventValidator {

    private final TicketEventRepository ticketEventRepository;

    @Autowired
    public TicketEventValidator(TicketEventRepository ticketEventRepository) {
        this.ticketEventRepository = ticketEventRepository;
    }

    public void validateTicketEventCreation(TicketEvent ticketEvent) {
        // Quy tắc 1: Tên sự kiện không được để trống và không quá 255 ký tự
        if (ticketEvent.getName() == null || ticketEvent.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên sự kiện không được để trống");
        }

        if (ticketEvent.getName().length() > 255) {
            throw new IllegalArgumentException("Tên sự kiện không được vượt quá 255 ký tự");
        }

        // Quy tắc 2: Mô tả độ dài không vượt quá 255 ký tự
        if (ticketEvent.getDescription() != null && ticketEvent.getDescription().length() > 255) {
            throw new IllegalArgumentException("Mô tả sự kiện không được vượt quá 255 ký tự");
        }

        // Quy tắc 3: Thời gian bắt đầu phải trước thời gian kết thúc
        if (ticketEvent.getStartTime() != null && ticketEvent.getEndTime() != null) {
            if (ticketEvent.getStartTime().isAfter(ticketEvent.getEndTime())) {
                throw new IllegalArgumentException("Thời gian bắt đầu sự kiện phải diễn ra trước thời gian kết thúc");
            }
        }

        // Quy tắc 4: Thời gian bắt đầu không được ở trong quá khứ
        if (ticketEvent.getStartTime() != null && ticketEvent.getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Thời gian bắt đầu sự kiện không được ở trong quá khứ");
        }

        // Quy tắc 5: Kiểm tra trùng lặp sự kiện
        boolean exist = ticketEventRepository.existsByNameAndStartTimeAndEndTime(
                ticketEvent.getName(),
                ticketEvent.getStartTime(),
                ticketEvent.getEndTime()
        );
        if (exist) {
            throw new IllegalArgumentException("Sự kiện đã tồn tại trong hệ thống");
        }
    }
}