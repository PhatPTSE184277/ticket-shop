package com.ticketShop.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * ENTITY: Đại diện cho một mẫu cấu hình phân (Bucket Configuration Template).
 *
 * Trong DDD, đây là một Entity vì nó có một định danh duy nhất (id) và vòng đời riêng (có thể được tạo, cập nhật, xóa).
 * Nó chứa các quy tắc nghiệp vụ cốt lõi để quyết định cách hệ thống tồn kho sẽ hoạt động.
 * Ví dụ: một sản phẩm "flash sale" có thể dùng một mẫu cấu hình khác với sản phẩm thông thường.
 * Lớp này chỉ chứa dữ liệu, logic nghiệp vụ sẽ được đặt trong Domain Service.
 */
@Data // Lombok: Tự động tạo getters, setters, toString(), equals(), và hashCode().
@Accessors(chain = true) // Lombok: Hỗ trợ fluent API (ví dụ: new Config().setId(1L).setBucketNum(8);)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventory_bucket_config")
public class InventoryBucketConfig {

    /**
     * Định danh duy nhất của cấu hình.
     * Tương ứng với cột `id` (PRIMARY KEY).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tên của mẫu cấu hình, giúp người vận hành dễ nhận biết.
     * Ví dụ: "Default Template", "Flash Sale Template".
     * Tương ứng với cột `template_name`.
     */
    @Column(name = "template_name")
    private String templateName;

    /**
     * Số lượng thùng (buckets) tối đa mà một sản phẩm có thể được chia ra theo mẫu này.
     * Đây là một tham số quan trọng để kiểm soát mức độ song song (concurrency).
     * Tương ứng với cột `bucket_num`.
     */
    @Column(name = "bucket_num")
    private Integer bucketNum;

    /**
     * Dung lượng (số lượng tồn kho) tối đa mà một thùng phân mảnh có thể chứa.
     * Giúp ngăn một thùng chứa quá nhiều tồn kho, đảm bảo sự phân bổ đều.
     * Tương ứng với cột `max_depth_num`.
     */
    @Column(name = "max_depth_num")
    private Integer maxDepthNum;

    /**
     * Dung lượng tối thiểu cần thiết để một thùng được coi là "hợp lệ" và được kích hoạt (online).
     * Ngăn chặn việc tạo ra các thùng "vụn" với quá ít tồn kho.
     * Tương ứng với cột `min_depth_num`.
     */
    @Column(name = "min_depth_num")
    private Integer minDepthNum;

    /**
     * Ngưỡng tồn kho để kích hoạt việc xem xét thu hẹp (offline) một thùng.
     * Khi tồn kho của một thùng thấp hơn giá trị này, nó sẽ bị đưa vào danh sách ứng viên để thu hồi.
     * Tương ứng với cột `threshold_value`.
     */
    @Column(name = "threshold_value")
    private Integer thresholdValue;

    /**
     * Tỷ lệ phần trăm tồn kho (1-100) kích hoạt việc mở rộng (scale-up).
     * Ví dụ: 40, nghĩa là khi tồn kho thùng còn dưới 40% dung lượng ban đầu, nó sẽ yêu cầu nạp thêm từ kho trung tâm.
     * Tương ứng với cột `back_source_proportion`.
     */
    @Column(name = "back_source_proportion")
    private Integer backSourceProportion;

    /**
     * Lượng tồn kho mặc định được nạp vào mỗi lần mở rộng, khi kho trung tâm còn nhiều hàng.
     * Đây là "bước nhảy" cố định để nhanh chóng bổ sung tồn kho cho các thùng.
     * Tương ứng với cột `back_source_step`.
     */
    @Column(name = "back_source_step")
    private Integer backSourceStep;

    /**
     * Cờ đánh dấu đây có phải là mẫu cấu hình mặc định cho toàn hệ thống hay không.
     * Chỉ nên có một mẫu mặc định tại một thời điểm.
     * Tương ứng với cột `is_default`.
     */
    @Column(name = "is_default")
    private Boolean isDefault;

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