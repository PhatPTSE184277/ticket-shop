package com.ticketShop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class CreateTicketItemRequest {
    @NotBlank(message = "Ticket item name cannot be empty")
    private String name;

    @Size(min = 0, max = 500, message = "Description must be between 0 and 500 characters")
    private String description;

    @NotNull
    @Min(1)
    private Integer stockInitial;

    @NotNull
    @Min(0)
    private Integer stockAvailable;

    @NotNull
    @Min(1)
    private Long priceOriginal;

    private Long priceFlash;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date saleStartTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date saleEndTime;

    private Boolean stockPrepared;
}
