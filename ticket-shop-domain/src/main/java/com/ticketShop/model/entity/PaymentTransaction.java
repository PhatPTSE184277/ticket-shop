package com.ticketShop.model.entity;

import com.ticketShop.model.enums.PaymentStatus;
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
@Table(name = "payment_transaction")
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus; // 0:INIT, 1:IN_PROGRESS, 2:SUCCESS, 3:FAILED

    @Column(name = "gateway_transaction_id")
    private String gatewayTransactionId;

    @Column(name = "payment_url")
    private String paymentUrl;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
