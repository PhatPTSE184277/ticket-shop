package com.ticketShop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateEventRequest {

    @Size(max = 255, message = "Tên sự kiện không được vượt quá 255 ký tự")
    private String name;

    @Size(min = 0, max = 500, message = "Mô tả sự kiện phải từ 0 đến 500 ký tự")
    private String description;

    @NotNull(message = "Thời gian bắt đầu không được để trống")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @NotNull(message = "Thời gian kết thúc không được để trống")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}