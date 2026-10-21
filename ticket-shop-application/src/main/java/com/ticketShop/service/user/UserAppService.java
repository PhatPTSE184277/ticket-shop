package com.ticketShop.service.user;

import com.ticketShop.model.command.auth.RegisterUserCommand;
import com.ticketShop.model.dto.AuthResponse;
import com.ticketShop.model.dto.UserDTO;
import com.ticketShop.model.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserAppService {
    UserDTO register(RegisterUserCommand command);
    AuthResponse login(String username, String password);
    AuthResponse refreshToken(String refreshToken);
    UserDTO getCurrentUser(Long userId);
    Page<UserDTO> getUsersByStatus(UserStatus status, Pageable pageable);
}