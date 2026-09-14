package com.ticketShop.model.entity;

import com.ticketShop.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ticket_order_202604")
public class TicketOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "user_id")
    private Long userId;

    // -- BỔ SUNG 2 TRƯỜNG NÀY ĐỂ KHỚP VỚI DATABASE --
    @Column(name = "ticket_id")
    private Long ticketId;

    @Column(name = "quantity")
    private Integer quantity;
    // ----------------------------------------------

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "terminal_id")
    private String terminalId;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "order_notes")
    private String orderNotes;

    /**
     * Trạng thái đơn hàng:
     * 0: PENDING (Đang chờ - Mới tạo)
     * 1: SUCCESS (Thanh toán thành công)
     * 2: CANCELLED (Người dùng chủ động hủy)
     * 3: EXPIRED (Hết hạn do không thanh toán kịp)
     * 4: REFUNDED (Đã hoàn tiền)
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
