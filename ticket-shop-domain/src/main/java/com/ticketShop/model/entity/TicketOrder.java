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

    @Column(name = "user_id", nullable = false)
    private Long userId; // ID người dùng đặt vé

    @Column(name = "order_number", nullable = false, unique = true, length = 50)
    private String orderNumber; // Mã đơn hàng duy nhất

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount; // Tổng số tiền của đơn hàng

    /**
     * Trạng thái đơn hàng:
     * 0: PENDING - Chờ thanh toán
     * 1: SUCCESS - Thành công
     * 2: CANCELLED - Đã hủy
     * 3: EXPIRED - Hết hạn
     * 4: REFUNDED - Đã hoàn tiền
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate; // Thời gian đặt vé

    @Column(name = "expire_at")
    private LocalDateTime expireAt; // Thời gian đơn hàng hết hạn thanh toán

    @Column(name = "order_notes", length = 255)
    private String orderNotes; // Ghi chú đơn hàng

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // Thời gian cập nhật gần nhất

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // Thời gian tạo đơn hàng
}