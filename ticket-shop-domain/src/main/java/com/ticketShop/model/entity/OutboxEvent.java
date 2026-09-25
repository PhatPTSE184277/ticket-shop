package com.ticketShop.model.entity;

import com.ticketShop.model.enums.OutboxEventStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

// 0 = PENDING, 1 = PUBLISHED
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "outbox_event", indexes = {
        @Index(name = "idx_status_created", columnList = "status, created_at")
})
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aggregate_id", nullable = false, length = 64)
    private String aggregateId; // ID aggregate liên quan

    @Column(name = "aggregate_type", nullable = false, length = 64)
    private String aggregateType; // Loại aggregate, ví dụ TICKET_ORDER

    @Column(name = "event_type", nullable = false, length = 64)
    private String eventType; // Loại event, ví dụ ORDER_PLACED

    @Column(name = "payload", nullable = false, columnDefinition = "JSON")
    private String payload; // Dữ liệu event dạng JSON

    /**
     * Trạng thái Outbox Event:
     * 0: PENDING - Chờ gửi
     * 1: PUBLISHED - Đã gửi
     * 2: FAILED - Gửi thất bại
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private OutboxEventStatus status;

    @Column(name = "retry_count", nullable = false)
    private int retryCount; // Số lần thử gửi lại

    @Column(name = "next_retry_at")
    private LocalDateTime nextRetryAt; // Thời gian retry tiếp theo

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;
}