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

    @Column(name = "payment_id", nullable = false, unique = true, length = 64)
    private String paymentId; // Mã thanh toán duy nhất

    @Column(name = "order_id", nullable = false)
    private Long orderId; // ID đơn hàng

    @Column(name = "user_id", nullable = false)
    private Long userId; // ID người dùng

    @Column(name = "amount", nullable = false)
    private BigDecimal amount; // Số tiền thanh toán

    @Column(name = "payment_method", nullable = false, length = 20)
    private String paymentMethod; // VNPAY, MOMO, LINKED_BANK...

    /**
     * Trạng thái thanh toán:
     * 0: INIT - Khởi tạo
     * 1: IN_PROGRESS - Đang xử lý
     * 2: SUCCESS - Thành công
     * 3: FAILED - Thất bại
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "gateway_transaction_id", length = 100)
    private String gatewayTransactionId; // Mã giao dịch từ cổng thanh toán

    @Column(name = "payment_url", columnDefinition = "TEXT")
    private String paymentUrl; // URL thanh toán

    @Column(name = "paid_at")
    private LocalDateTime paidAt; // Thời gian thanh toán thành công

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}