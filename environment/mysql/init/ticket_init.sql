-- 1. TÁI TẠO DATABASE
DROP DATABASE IF EXISTS `ticket-shop`;
CREATE DATABASE `ticket-shop`
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci;

USE `ticket-shop`;


-- 2. BẢNG QUẢN LÝ NGƯỜI DÙNG (users)
CREATE TABLE IF NOT EXISTS `users` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Khóa chính - ID người dùng',
    `username` VARCHAR(50) NOT NULL COMMENT 'Tên đăng nhập',
    `email` VARCHAR(100) NOT NULL COMMENT 'Địa chỉ email',
    `phone` VARCHAR(20) NOT NULL COMMENT 'Số điện thoại',
    `password` VARCHAR(255) NOT NULL COMMENT 'Mật khẩu đã mã hóa',
    `full_name` VARCHAR(100) NOT NULL COMMENT 'Họ và tên người dùng',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT 'Trạng thái tài khoản (0: Không hoạt động, 1: Hoạt động, 2: Bị khóa)',
    `role` VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER' COMMENT 'Vai trò người dùng (CUSTOMER, STAFF, ADMIN)',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời gian cập nhật gần nhất',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời gian tạo tài khoản',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    UNIQUE KEY `uk_phone` (`phone`),
    KEY `idx_status` (`status`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'Bảng quản lý người dùng';


-- 3. BẢNG SỰ KIỆN MỞ BÁN VÉ (ticket_event)
CREATE TABLE IF NOT EXISTS `ticket_event` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Khóa chính - ID sự kiện',
    `name` VARCHAR(100) NOT NULL COMMENT 'Tên sự kiện',
    `description` TEXT NULL COMMENT 'Mô tả sự kiện',
    `start_time` DATETIME NOT NULL COMMENT 'Thời gian bắt đầu mở bán',
    `end_time` DATETIME NOT NULL COMMENT 'Thời gian kết thúc mở bán',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT 'Trạng thái sự kiện (0: Không hoạt động, 1: Đang hoạt động, 2: Đã kết thúc, 3: Đã xóa)',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời gian cập nhật gần nhất',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời gian tạo sự kiện',
    PRIMARY KEY (`id`),
    KEY `idx_status_time` (`status`, `start_time`, `end_time`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'Bảng quản lý sự kiện mở bán vé';


-- 4. BẢNG CHI TIẾT LOẠI VÉ (ticket_item)
CREATE TABLE IF NOT EXISTS `ticket_item` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Khóa chính - ID loại vé',
    `name` VARCHAR(100) NOT NULL COMMENT 'Tên loại vé',
    `description` TEXT NULL COMMENT 'Mô tả loại vé',
    `stock_initial` INT NOT NULL DEFAULT 0 COMMENT 'Số lượng vé ban đầu',
    `stock_available` INT NOT NULL DEFAULT 0 COMMENT 'Số lượng vé còn lại',
    `is_stock_prepared` BOOLEAN NOT NULL DEFAULT 0 COMMENT 'Đánh dấu kho vé đã được chuẩn bị trước',
    `price_original` DECIMAL(12,2) NOT NULL COMMENT 'Giá vé gốc',
    `price_flash` DECIMAL(12,2) NOT NULL COMMENT 'Giá vé trong chương trình flash sale',
    `sale_start_time` DATETIME NOT NULL COMMENT 'Thời gian bắt đầu bán loại vé',
    `sale_end_time` DATETIME NOT NULL COMMENT 'Thời gian kết thúc bán loại vé',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT 'Trạng thái loại vé (0: Không hoạt động, 1: Đang hoạt động, 2: Hết vé, 3: Đã xóa)',
    `version` BIGINT NOT NULL DEFAULT 0 COMMENT 'Version phục vụ optimistic locking',
    `event_id` BIGINT NOT NULL COMMENT 'ID sự kiện mở bán vé',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời gian cập nhật gần nhất',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời gian tạo loại vé',
    PRIMARY KEY (`id`),
    KEY `idx_event_id` (`event_id`),
    KEY `idx_status_sale_time` (`status`, `sale_start_time`, `sale_end_time`),
    CONSTRAINT `fk_ticket_item_event`
    FOREIGN KEY (`event_id`) REFERENCES `ticket_event` (`id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'Bảng quản lý các loại vé thuộc sự kiện';


-- 5. BẢNG ĐƠN HÀNG (ticket_order_202604)
CREATE TABLE IF NOT EXISTS `ticket_order_202604` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Khóa chính - ID đơn hàng',
    `user_id` BIGINT NOT NULL COMMENT 'ID người dùng đặt vé',
    `order_number` VARCHAR(50) NOT NULL COMMENT 'Mã đơn hàng duy nhất',
    `total_amount` DECIMAL(12,2) NOT NULL COMMENT 'Tổng số tiền của đơn hàng',
    `order_status` TINYINT NOT NULL DEFAULT 0 COMMENT 'Trạng thái đơn hàng (0: Chờ thanh toán, 1: Thành công, 2: Đã hủy, 3: Hết hạn)',
    `order_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời gian đặt vé',
    `expire_at` DATETIME NULL COMMENT 'Thời gian đơn hàng hết hạn thanh toán',
    `order_notes` VARCHAR(255) NULL COMMENT 'Ghi chú đơn hàng',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời gian cập nhật gần nhất',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời gian tạo đơn hàng',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_number` (`order_number`),
    KEY `idx_order_date` (`order_date`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status_expire_at` (`order_status`, `expire_at`),
    CONSTRAINT `fk_order_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'Bảng quản lý đơn hàng';


-- 6. BẢNG CHI TIẾT ĐƠN HÀNG (ticket_order_details_202604)
CREATE TABLE IF NOT EXISTS `ticket_order_details_202604` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Khóa chính - ID chi tiết đơn hàng',
    `order_id` BIGINT NOT NULL COMMENT 'ID đơn hàng',
    `ticket_item_id` BIGINT NOT NULL COMMENT 'ID loại vé',
    `quantity` INT NOT NULL DEFAULT 1 COMMENT 'Số lượng vé',
    `unit_price` DECIMAL(12,2) NOT NULL COMMENT 'Đơn giá vé tại thời điểm đặt',
    `total_price` DECIMAL(12,2) NOT NULL COMMENT 'Tổng tiền của chi tiết đơn hàng',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời gian cập nhật gần nhất',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời gian tạo chi tiết đơn hàng',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_ticket_item_id` (`ticket_item_id`),
    CONSTRAINT `fk_order_detail_order`
    FOREIGN KEY (`order_id`) REFERENCES `ticket_order_202604` (`id`),
    CONSTRAINT `fk_order_detail_ticket_item`
    FOREIGN KEY (`ticket_item_id`) REFERENCES `ticket_item` (`id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'Bảng chi tiết các vé trong đơn hàng';


-- 7. BẢNG GIAO DỊCH THANH TOÁN (payment_transaction)
CREATE TABLE IF NOT EXISTS `payment_transaction` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Khóa chính - ID giao dịch',
    `payment_id` VARCHAR(64) NOT NULL COMMENT 'Mã thanh toán duy nhất',
    `order_id` BIGINT NOT NULL COMMENT 'ID đơn hàng liên kết',
    `user_id` BIGINT NOT NULL COMMENT 'ID người dùng thực hiện thanh toán',
    `amount` DECIMAL(12,2) NOT NULL COMMENT 'Số tiền thanh toán',
    `payment_method` VARCHAR(20) NOT NULL COMMENT 'Phương thức thanh toán (VNPAY, MOMO, LINKED_BANK...)',
    `payment_status` TINYINT NOT NULL DEFAULT 0 COMMENT 'Trạng thái thanh toán (0: Khởi tạo, 1: Đang xử lý, 2: Thành công, 3: Thất bại, 4: Đã hoàn tiền)',
    `gateway_transaction_id` VARCHAR(100) NULL COMMENT 'Mã giao dịch do cổng thanh toán trả về',
    `payment_url` TEXT NULL COMMENT 'Đường dẫn thanh toán trả về cho người dùng',
    `paid_at` DATETIME NULL COMMENT 'Thời gian thanh toán thành công',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời gian cập nhật gần nhất',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời gian tạo giao dịch',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_payment_id` (`payment_id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_payment_status` (`payment_status`),
    CONSTRAINT `fk_payment_order`
    FOREIGN KEY (`order_id`) REFERENCES `ticket_order_202604` (`id`),
    CONSTRAINT `fk_payment_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'Bảng lưu lịch sử giao dịch thanh toán';


-- 8. BẢNG HÀNG ĐỢI XỬ LÝ ĐƠN HÀNG (order_queue)
CREATE TABLE IF NOT EXISTS `order_queue` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Khóa chính - ID yêu cầu trong hàng đợi',
    `token` VARCHAR(64) NOT NULL COMMENT 'Token duy nhất của yêu cầu đặt vé',
    `ticket_item_id` BIGINT NOT NULL COMMENT 'ID loại vé cần đặt',
    `quantity` INT NOT NULL COMMENT 'Số lượng vé cần đặt',
    `user_id` BIGINT NOT NULL COMMENT 'ID người dùng đặt vé',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT 'Trạng thái xử lý (0: Chờ xử lý, 1: Thành công, 2: Thất bại)',
    `order_id` BIGINT NULL COMMENT 'ID đơn hàng được tạo sau khi xử lý',
    `message` VARCHAR(255) NULL COMMENT 'Thông báo kết quả xử lý',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời gian tạo yêu cầu',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời gian cập nhật gần nhất',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_queue_token` (`token`),
    KEY `idx_queue_status` (`status`),
    KEY `idx_queue_ticket_item` (`ticket_item_id`),
    KEY `idx_queue_user` (`user_id`),
    CONSTRAINT `fk_queue_ticket_item`
    FOREIGN KEY (`ticket_item_id`) REFERENCES `ticket_item` (`id`),
    CONSTRAINT `fk_queue_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_queue_order`
    FOREIGN KEY (`order_id`) REFERENCES `ticket_order_202604` (`id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'Bảng hàng đợi xử lý yêu cầu đặt vé';


-- 9. BẢNG OUTBOX EVENT (outbox_event)
CREATE TABLE IF NOT EXISTS `outbox_event` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Khóa chính - ID sự kiện',
    `aggregate_id` VARCHAR(64) NOT NULL COMMENT 'ID đối tượng nghiệp vụ liên quan đến sự kiện',
    `aggregate_type` VARCHAR(64) NOT NULL COMMENT 'Loại aggregate, ví dụ TICKET_ORDER',
    `event_type` VARCHAR(64) NOT NULL COMMENT 'Loại sự kiện nghiệp vụ, ví dụ ORDER_PLACED',
    `payload` JSON NOT NULL COMMENT 'Dữ liệu sự kiện dưới dạng JSON',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT 'Trạng thái sự kiện (0: Chờ gửi, 1: Đã gửi, 2: Gửi thất bại)',
    `retry_count` INT NOT NULL DEFAULT 0 COMMENT 'Số lần thử gửi lại sự kiện',
    `next_retry_at` DATETIME NULL COMMENT 'Thời gian thử gửi lại tiếp theo',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời gian tạo sự kiện',
    `published_at` DATETIME NULL COMMENT 'Thời gian Kafka xác nhận sự kiện đã được gửi thành công',
    PRIMARY KEY (`id`),
    KEY `idx_status_created` (`status`, `created_at`),
    KEY `idx_status_retry` (`status`, `next_retry_at`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'Bảng Outbox lưu sự kiện chờ gửi đến Kafka';


-- 10. BẢNG CHỐNG XỬ LÝ TRÙNG LẶP (idempotency_key)
CREATE TABLE IF NOT EXISTS `idempotency_key` (
    `token` VARCHAR(64) NOT NULL COMMENT 'Token duy nhất của yêu cầu đặt vé',
    `user_id` BIGINT NOT NULL COMMENT 'ID người dùng gửi yêu cầu',
    `request_hash` VARCHAR(64) NULL COMMENT 'Hash của request để kiểm tra request bị thay đổi',
    `order_id` BIGINT NULL COMMENT 'ID đơn hàng được tạo bởi request',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT 'Trạng thái request (0: Đang xử lý, 1: Thành công, 2: Thất bại)',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời gian tạo idempotency key',
    `expires_at` DATETIME NOT NULL COMMENT 'Thời gian hết hạn của idempotency key',
    PRIMARY KEY (`token`),
    KEY `idx_idempotency_user` (`user_id`),
    KEY `idx_idempotency_expires_at` (`expires_at`),
    CONSTRAINT `fk_idempotency_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_idempotency_order`
    FOREIGN KEY (`order_id`) REFERENCES `ticket_order_202604` (`id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'Bảng lưu khóa chống xử lý trùng lặp';


-- INSERT MOCK DATA

-- Dữ liệu mẫu người dùng
INSERT INTO `users`
(`id`, `username`, `email`, `phone`, `password`, `full_name`, `status`, `role`)
VALUES
    (1001, 'nguyenvana', 'nguyenvana@example.com', '0901234567', '$2a$10$examplePasswordHash', 'Nguyễn Văn A', 1, 'CUSTOMER'),
    (1002, 'admin', 'admin@example.com', '0901234568', '$2a$10$examplePasswordHash', 'Quản trị viên', 1, 'ADMIN');


-- Dữ liệu mẫu sự kiện
INSERT INTO `ticket_event`
(`name`, `description`, `start_time`, `end_time`, `status`)
VALUES
    ('Flash Sale Vé 12/12', 'Đợt mở bán vé flash sale ngày 12/12', '2026-12-12 00:00:00', '2026-12-12 23:59:59', 1),
    ('Flash Sale Vé Năm Mới', 'Đợt mở bán vé đặc biệt đầu năm mới', '2027-01-01 00:00:00', '2027-01-01 23:59:59', 1);


-- Dữ liệu mẫu loại vé
INSERT INTO `ticket_item`
(`name`, `description`, `stock_initial`, `stock_available`, `is_stock_prepared`,
 `price_original`, `price_flash`, `sale_start_time`, `sale_end_time`,
 `status`, `version`, `event_id`)
VALUES
    ('Vé 12/12 - Hạng Phổ Thông', 'Vé phổ thông cho sự kiện ngày 12/12', 1000, 1000, 1,
     100000.00, 10000.00, '2026-12-12 00:00:00', '2026-12-12 23:59:59',
     1, 0, 1),

    ('Vé 12/12 - Hạng VIP', 'Vé VIP cho sự kiện ngày 12/12', 500, 500, 1,
     200000.00, 15000.00, '2026-12-12 00:00:00', '2026-12-12 23:59:59',
     1, 0, 1),

    ('Vé Năm Mới - Hạng Phổ Thông', 'Vé phổ thông cho sự kiện đầu năm mới', 2000, 2000, 1,
     100000.00, 10000.00, '2027-01-01 00:00:00', '2027-01-01 23:59:59',
     1, 0, 2),

    ('Vé Năm Mới - Hạng VIP', 'Vé VIP cho sự kiện đầu năm mới', 1000, 1000, 1,
     200000.00, 15000.00, '2027-01-01 00:00:00', '2027-01-01 23:59:59',
     1, 0, 2);


-- Dữ liệu mẫu đơn hàng
INSERT INTO `ticket_order_202604`
(`user_id`, `order_number`, `total_amount`, `order_status`, `order_date`, `expire_at`, `order_notes`)
VALUES
    (1001, 'ORD202609250001', 30000.00, 1, '2026-09-25 10:00:00', NULL, 'Đặt vé flash sale');