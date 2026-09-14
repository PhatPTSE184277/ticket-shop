package com.ticketShop.model.entity;

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
@Table(name = "order_queue")
public class OrderQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", unique = true, nullable = false, length = 64)
    private String token;

    @Column(name = "ticket_id", nullable = false)
    private Long ticketId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    // 0=PENDING, 1=SUCCESS, 2=FAILED
    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "message")
    private String message;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
