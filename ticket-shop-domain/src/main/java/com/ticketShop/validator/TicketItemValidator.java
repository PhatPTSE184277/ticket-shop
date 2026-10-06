package com.ticketShop.validator;

import com.ticketShop.model.entity.TicketItem;
import com.ticketShop.repository.TicketEventRepository;
import com.ticketShop.repository.TicketItemRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TicketItemValidator {
    private final TicketItemRepository ticketItemRepository;
    private final TicketEventRepository ticketEventRepository;

    public TicketItemValidator(TicketItemRepository ticketItemRepository, TicketEventRepository ticketEventRepository) {
        this.ticketItemRepository = ticketItemRepository;
        this.ticketEventRepository = ticketEventRepository;
    }

    public void validateTicketItemCreation(TicketItem ticketItem) {
        // Quy tắc 1: Tên hạng vé không được để trống
        if (ticketItem.getName() == null || ticketItem.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên hạng vé không được để trống");
        }

        // Quy tắc 2: Số lượng vé ban đầu phải lớn hơn 0
        if (ticketItem.getStockInitial() <= 0) {
            throw new IllegalArgumentException("Số lượng vé phát hành ban đầu phải lớn hơn 0");
        }

        // Quy tắc 3: Số lượng vé khả dụng phải >= 0 và không vượt quá số lượng ban đầu
        if (ticketItem.getStockAvailable() < 0) {
            throw new IllegalArgumentException("Số lượng vé khả dụng không được nhỏ hơn 0");
        }

        if (ticketItem.getStockAvailable() > ticketItem.getStockInitial()) {
            throw new IllegalArgumentException("Số lượng vé khả dụng không được lớn hơn số lượng vé phát hành ban đầu");
        }

        // Quy tắc 4: Giá vé gốc phải lớn hơn 0
        if (ticketItem.getPriceOriginal() == null || ticketItem.getPriceOriginal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Giá vé gốc phải lớn hơn 0");
        }

        // Quy tắc 5: Thời gian mở bán phải trước thời gian kết thúc bán
        if (ticketItem.getSaleStartTime() != null && ticketItem.getSaleEndTime() != null) {
            if (ticketItem.getSaleStartTime().isAfter(ticketItem.getSaleEndTime())) {
                throw new IllegalArgumentException("Thời gian mở bán vé phải trước thời gian kết thúc bán vé");
            }
        }

        // Quy tắc 6: Event ID không tồn tại
        if (ticketItem.getEventId() != null) {
            boolean exist = ticketEventRepository.existsById(ticketItem.getEventId());

            if (!exist) {
                throw new IllegalArgumentException("Sự kiện có ID " + ticketItem.getEventId() + " không tồn tại trên hệ thống");
            }
        }

        // Quy tắc 7: Kiểm tra trùng tên hạng vé TRONG CÙNG 1 SỰ KIỆN (eventId)
        if (ticketItem.getEventId() != null){
            boolean exist = ticketItemRepository.existsByEventIdAndName(ticketItem.getEventId(), ticketItem.getName().trim());

            if (exist){
                throw new IllegalArgumentException("Hạng vé '" + ticketItem.getName() + "' đã tồn tại trong sự kiện này");
            }
        }
    }
}