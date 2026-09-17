package com.ticketShop.model.entity;

import com.ticketShop.model.enums.OutboxEventStatus;
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
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticket_id", nullable = false)
    private Long ticketId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "booking_code", length = 64, nullable = false, unique = true)
    private String bookingCode;

    // 0=PENDING, 1=CONFIRMED, 2=CANCELLED
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private OutboxEventStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}