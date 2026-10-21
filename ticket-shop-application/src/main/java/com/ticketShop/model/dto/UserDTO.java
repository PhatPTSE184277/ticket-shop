package com.ticketShop.model.dto;
import com.ticketShop.model.enums.UserRole;
import com.ticketShop.model.enums.UserStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String fullName;
    private UserStatus status;
    private UserRole role;
    private LocalDateTime createdAt;
}