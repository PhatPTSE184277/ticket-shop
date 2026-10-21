package com.ticketShop.mapper;

import com.ticketShop.model.command.auth.RegisterUserCommand;
import com.ticketShop.model.dto.UserDTO;
import com.ticketShop.model.entity.User;
import com.ticketShop.model.enums.UserRole;
import com.ticketShop.model.enums.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterUserCommand command, String encodedPassword) {
        if (command == null) return null;
        User user = new User();
        user.setUsername(command.getUsername());
        user.setEmail(command.getEmail());
        user.setPhone(command.getPhone());
        user.setPassword(encodedPassword);
        user.setFullName(command.getFullName());
        user.setStatus(UserStatus.ACTIVE);
        user.setRole(UserRole.CUSTOMER);
        return user;
    }

    public UserDTO toDTO(User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setFullName(user.getFullName());
        dto.setStatus(user.getStatus());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}