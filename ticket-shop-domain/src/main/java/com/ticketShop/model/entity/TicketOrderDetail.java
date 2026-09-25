package com.ticketShop.model.entity;

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
@Table(name = "ticket_order_details_202604")
public class TicketOrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId; // ID đơn hàng

    @Column(name = "ticket_item_id", nullable = false)
    private Long ticketItemId; // ID loại vé

    @Column(name = "quantity", nullable = false)
    private Integer quantity; // Số lượng vé

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice; // Đơn giá tại thời điểm đặt

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice; // Tổng tiền

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // Thời gian cập nhật gần nhất

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // Thời gian tạo
}