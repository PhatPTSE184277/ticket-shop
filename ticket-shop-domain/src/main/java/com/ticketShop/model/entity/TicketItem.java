package com.ticketShop.model.entity;

import com.ticketShop.model.enums.TicketItemStatus;
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
@Table(name = "ticket_item")
public class TicketItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name; // Tên loại vé

    @Column(name = "description")
    private String description; // Mô tả loại vé

    @Column(name = "stock_initial", nullable = false)
    private int stockInitial; // Số lượng vé ban đầu

    @Column(name = "stock_available", nullable = false)
    private int stockAvailable; // Số lượng vé còn lại

    @Column(name = "is_stock_prepared", nullable = false)
    private boolean stockPrepared; // Đã chuẩn bị kho vé?

    @Column(name = "price_original", nullable = false)
    private BigDecimal priceOriginal; // Giá vé gốc

    @Column(name = "price_flash", nullable = false)
    private BigDecimal priceFlash; // Giá vé trong chương trình flash sale

    @Column(name = "sale_start_time", nullable = false)
    private LocalDateTime saleStartTime; // Thời gian bắt đầu bán loại vé

    @Column(name = "sale_end_time", nullable = false)
    private LocalDateTime saleEndTime; // Thời gian kết thúc bán loại vé

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private TicketItemStatus status; // Trạng thái loại vé

    @Version
    @Column(name = "version", nullable = false)
    private Long version; // Optimistic locking

    @Column(name = "event_id", nullable = false)
    private Long eventId; // ID sự kiện mở bán vé

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // Thời gian cập nhật gần nhất

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // Thời gian tạo loại vé
}