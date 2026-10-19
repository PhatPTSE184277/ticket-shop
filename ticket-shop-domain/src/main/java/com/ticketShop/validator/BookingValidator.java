
package com.ticketShop.validator;

import com.ticketShop.model.enums.TicketStatus;
import com.ticketShop.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookingValidator {

    private final TicketRepository ticketRepository;

    @Autowired
    public BookingValidator(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public void validateCreateBooking(Long ticketId, int quantity) {
        if (ticketId == null || ticketId <= 0) {
            throw new IllegalArgumentException("ID vé không hợp lệ");
        }

        if (!ticketRepository.existsByIdAndStatus(ticketId, TicketStatus.ACTIVE)) {
            throw new IllegalArgumentException("Vé không tồn tại hoặc không ở trạng thái hoạt động");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Số lượng vé phải lớn hơn 0");
        }

        if (quantity > 10) {
            throw new IllegalArgumentException("Không thể đặt quá 10 vé trong một lần");
        }
    }
}
