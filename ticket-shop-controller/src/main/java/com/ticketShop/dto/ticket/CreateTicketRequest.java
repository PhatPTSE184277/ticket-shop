package com.ticketShop.dto.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class CreateTicketRequest {

    @NotNull(message = "ID sự kiện không được để trống")
    private Long eventId;

    @NotBlank(message = "Tên loại vé không được để trống")
    private String name;

    @Size(min = 0, max = 500, message = "Mô tả loại vé phải từ 0 đến 500 ký tự")
    private String description;

    @NotNull(message = "Số lượng vé ban đầu không được để trống")
    @Min(value = 1, message = "Số lượng vé ban đầu phải từ 1 trở lên")
    private Integer stockInitial;

    @NotNull(message = "Số lượng vé còn lại không được để trống")
    @Min(value = 0, message = "Số lượng vé còn lại không được nhỏ hơn 0")
    private Integer stockAvailable;

    @NotNull(message = "Giá vé gốc không được để trống")
    @Min(value = 1, message = "Giá vé gốc phải lớn hơn 0")
    private Long priceOriginal;

    private Long priceFlash;

    @NotNull(message = "Thời gian bắt đầu bán vé không được để trống")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date saleStartTime;

    @NotNull(message = "Thời gian kết thúc bán vé không được để trống")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date saleEndTime;

    private Boolean stockPrepared;
}