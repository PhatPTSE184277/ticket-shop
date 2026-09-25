package com.ticketShop.model.entity;

import com.ticketShop.model.enums.OrderQueueStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_queue")
public class OrderQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", unique = true, nullable = false, length = 64)
    private String token; // Token định danh request đặt vé

    @Column(name = "ticket_item_id", nullable = false)
    private Long ticketItemId; // ID loại vé

    @Column(name = "quantity", nullable = false)
    private Integer quantity; // Số lượng vé cần đặt

    @Column(name = "user_id", nullable = false)
    private Long userId; // ID người dùng

    /**
     * Trạng thái xử lý request:
     * 0: PENDING - Đang chờ xử lý
     * 1: SUCCESS - Xử lý thành công
     * 2: FAILED - Xử lý thất bại
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private OrderQueueStatus status;

    @Column(name = "order_id")
    private Long orderId; // ID đơn hàng được tạo sau khi xử lý thành công

    @Column(name = "message")
    private String message; // Thông báo kết quả xử lý

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // Thời gian tạo request

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // Thời gian cập nhật gần nhất
}