package com.ticketShop.validator;

import com.ticketShop.model.entity.Event;
import com.ticketShop.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    private final EventRepository eventRepository;

    @Autowired
    public EventValidator(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void validateEventCreation(Event event) {
        // Quy tắc 1: Tên sự kiện không được để trống và không quá 255 ký tự
        if (event.getName() == null || event.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên sự kiện không được để trống");
        }

        if (event.getName().length() > 255) {
            throw new IllegalArgumentException("Tên sự kiện không được vượt quá 255 ký tự");
        }

        // Quy tắc 2: Mô tả độ dài không vượt quá 255 ký tự
        if (event.getDescription() != null && event.getDescription().length() > 255) {
            throw new IllegalArgumentException("Mô tả sự kiện không được vượt quá 255 ký tự");
        }

        // Quy tắc 3: Thời gian bắt đầu phải trước thời gian kết thúc
        if (event.getStartTime() != null && event.getEndTime() != null) {
            if (event.getStartTime().isAfter(event.getEndTime())) {
                throw new IllegalArgumentException("Thời gian bắt đầu sự kiện phải diễn ra trước thời gian kết thúc");
            }
        }

        // Quy tắc 4: Thời gian bắt đầu không được ở trong quá khứ
        if (event.getStartTime() != null && event.getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Thời gian bắt đầu sự kiện không được ở trong quá khứ");
        }

        // Quy tắc 5: Kiểm tra trùng lặp sự kiện
        boolean exist = eventRepository.existsByNameAndStartTimeAndEndTime(
                event.getName(),
                event.getStartTime(),
                event.getEndTime()
        );
        if (exist) {
            throw new IllegalArgumentException("Sự kiện đã tồn tại trong hệ thống");
        }
    }
}