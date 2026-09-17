package com.ticketShop.model.entity;

import com.ticketShop.model.enums.TicketStatus;
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
public class TicketDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "stock_initial")
    private int stockInitial;        // Số lượng vé ban đầu

    @Column(name = "stock_available")
    private int stockAvailable;      // Số lượng vé còn lại

    @Column(name = "is_stock_prepared")
    private boolean isStockPrepared; // Đã chuẩn bị kho?

    @Column(name = "price_original")
    private BigDecimal priceOriginal;      // Giá gốc

    @Column(name = "price_flash")
    private BigDecimal priceFlash;         // Giá flash sale

    @Column(name = "sale_start_time")
    private LocalDateTime saleStartTime;      // Thời gian bắt đầu bán

    @Column(name = "sale_end_time")
    private LocalDateTime saleEndTime;        // Thời gian kết thúc bán

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private TicketStatus status;              // 0=INACTIVE, 1=ACTIVE, 2=DELETED

    @Column(name = "activity_id")
    private Long activityId;         // Có thể là FK tới Ticket

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

//    @Override
//    public String toString() {
//        return "TicketDetail{id=" + id + ", name='" + name + "', otherField=1}";
//    }
}