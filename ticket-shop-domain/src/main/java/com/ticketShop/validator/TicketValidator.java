package com.ticketShop.validator;

import com.ticketShop.model.entity.Ticket;
import com.ticketShop.repository.EventRepository;
import com.ticketShop.repository.TicketRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TicketValidator {
    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;

    public TicketValidator(TicketRepository ticketRepository, EventRepository eventRepository) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
    }

    public void validateTicketCreation(Ticket ticket) {
        // Quy tắc 1: Tên hạng vé không được để trống
        if (ticket.getName() == null || ticket.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên hạng vé không được để trống");
        }

        // Quy tắc 2: Số lượng vé ban đầu phải lớn hơn 0
        if (ticket.getStockInitial() <= 0) {
            throw new IllegalArgumentException("Số lượng vé phát hành ban đầu phải lớn hơn 0");
        }

        // Quy tắc 3: Số lượng vé khả dụng phải >= 0 và không vượt quá số lượng ban đầu
        if (ticket.getStockAvailable() < 0) {
            throw new IllegalArgumentException("Số lượng vé khả dụng không được nhỏ hơn 0");
        }

        if (ticket.getStockAvailable() > ticket.getStockInitial()) {
            throw new IllegalArgumentException("Số lượng vé khả dụng không được lớn hơn số lượng vé phát hành ban đầu");
        }

        // Quy tắc 4: Giá vé gốc phải lớn hơn 0
        if (ticket.getPriceOriginal() == null || ticket.getPriceOriginal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Giá vé gốc phải lớn hơn 0");
        }

        // Quy tắc 5: Thời gian mở bán phải trước thời gian kết thúc bán
        if (ticket.getSaleStartTime() != null && ticket.getSaleEndTime() != null) {
            if (ticket.getSaleStartTime().isAfter(ticket.getSaleEndTime())) {
                throw new IllegalArgumentException("Thời gian mở bán vé phải trước thời gian kết thúc bán vé");
            }
        }

        // Quy tắc 6: Event ID không tồn tại
        if (ticket.getEventId() != null) {
            boolean exist = eventRepository.existsById(ticket.getEventId());

            if (!exist) {
                throw new IllegalArgumentException("Sự kiện có ID " + ticket.getEventId() + " không tồn tại trên hệ thống");
            }
        }

        // Quy tắc 7: Kiểm tra trùng tên hạng vé TRONG CÙNG 1 SỰ KIỆN (eventId)
        if (ticket.getEventId() != null){
            boolean exist = ticketRepository.existsByEventIdAndName(ticket.getEventId(), ticket.getName().trim());

            if (exist){
                throw new IllegalArgumentException("Hạng vé '" + ticket.getName() + "' đã tồn tại trong sự kiện này");
            }
        }
    }
}