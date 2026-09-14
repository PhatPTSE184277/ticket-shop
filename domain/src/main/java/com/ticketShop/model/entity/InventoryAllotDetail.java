package com.ticketShop.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * ENTITY: Đại diện cho một bản ghi lịch sử phân bổ/nhập kho.
 *
 * Mỗi bản ghi là một bằng chứng không thể thay đổi về một lần tăng tồn kho.
 * Nó có định danh (id) và có constraint UNIQUE trên `inventor_no` để đảm bảo tính lũy đẳng (Idempotency).
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventory_allot_detail", uniqueConstraints = {
        @UniqueConstraint(name = "uk_inventor_no", columnNames = {"inventor_no"})
})
public class InventoryAllotDetail {

    /**
     * Định danh duy nhất của bản ghi lịch sử.
     * Tương ứng với cột `id` (PRIMARY KEY).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ID của sản phẩm (SKU) được nhập kho.
     * Tương ứng với cột `sku_id`.
     */
    @Column(name = "sku_id")
    private String skuId;

    /**
     * Mã nghiệp vụ duy nhất cho lần nhập kho này (Idempotency Key).
     * Đây là "chìa khóa" để chống trùng lặp yêu cầu.
     * Ví dụ: Mã phiếu nhập kho từ hệ thống WMS.
     * Tương ứng với cột `inventor_no` (UNIQUE KEY).
     */
    @Column(name = "inventor_no", nullable = false, unique = true)
    private String inventorNo;

    /**
     * ID của người bán (seller) sở hữu sản phẩm này.
     * Tương ứng với cột `seller_id`.
     */
    @Column(name = "seller_id")
    private String sellerId;

    /**
     * Số lượng tồn kho được thay đổi trong lần nhập kho này.
     * Tương ứng với cột `inventor_num` .
     */
    @Column(name = "inventor_num")
    private Integer inventorNum;

    // --- Các trường metadata chung ---

    /**
     * Phiên bản của bản ghi, dùng cho cơ chế khóa lạc quan (Optimistic Locking).
     * Tương ứng với cột `version_id`.
     */
    @Version
    @Column(name = "version_id")
    private Integer versionId;

    /**
     * Cờ xóa mềm. 1 = đã xóa, 0 = đang hoạt động.
     * Tương ứng với cột `del_flag`.
     */
    @Column(name = "del_flag")
    private Integer delFlag;

    /**
     * ID của người tạo bản ghi.
     * Tương ứng với cột `create_user`.
     */
    @Column(name = "create_user")
    private Long createUser;

    /**
     * Thời gian tạo bản ghi.
     * Tương ứng với cột `create_time`.
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * ID của người cập nhật bản ghi lần cuối.
     * Tương ứng với cột `update_user`.
     */
    @Column(name = "update_user")
    private Long updateUser;

    /**
     * Thời gian cập nhật bản ghi lần cuối.
     * Tương ứng với cột `update_time`.
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}