package com.ticketShop.model.entity;

import com.ticketShop.model.enums.IdempotencyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "idempotency_key")
public class IdempotencyKey {

    @Id
    @Column(name = "token", length = 64, nullable = false)
    private String token; // Token duy nhất của request

    @Column(name = "user_id", nullable = false)
    private Long userId; // ID người dùng gửi request

    @Column(name = "request_hash", length = 64)
    private String requestHash; // Hash của request

    @Column(name = "order_id")
    private Long orderId; // ID đơn hàng được tạo

    /**
     * Trạng thái request:
     * 0: PENDING - Đang xử lý
     * 1: SUCCESS - Thành công
     * 2: FAILED - Thất bại
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private IdempotencyStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
}