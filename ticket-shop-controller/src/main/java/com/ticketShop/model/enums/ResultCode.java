package com.ticketShop.model.enums;

/**
 * Trả về mã trạng thái
 * Chữ số đầu tiên: 1: Sản phẩm; 2: Người dùng; 3: Giao dịch,
 * 4: Khuyến mãi, 5: Cửa hàng, 6: Trang web, 7: Cài đặt, 8: Khác
 *
 * @author tanphat
 */
public enum ResultCode {

    /**
     * Mã trạng thái thành công
     */
    SUCCESS(200, "Thành công"),

    /**
     * Tham số bất thường
     */
    PARAMS_ERROR(4002, "Tham số bất thường"),

    /**
     * Mã lỗi trả về
     */
    DEMO_SITE_FORBIDDEN_ERROR(4001, "Trang web demo không được phép sử dụng"),

    /**
     * Mã lỗi trả về
     */
    ERROR(400, "Máy chủ bận, vui lòng thử lại sau"),

    /**
     * Tham số
     */
    PRODUCT_PARAMETER_SAVE_ERROR(12001, "Thêm tham số thất bại"),
    PRODUCT_PARAMETER_UPDATE_ERROR(12002, "Chỉnh sửa tham số thất bại"),

    /**
     * Ngoại lệ hệ thống
     */
    RATE_LIMIT_ERROR(1003, "Truy cập quá thường xuyên, vui lòng thử lại sau"),
    ;


    private final Integer code;
    private final String message;


    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }
}

