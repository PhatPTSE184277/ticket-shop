package com.ticketShop.http;

import com.ticketShop.dto.auth.UserLoginRequest;
import com.ticketShop.dto.auth.UserRegistrationRequest;
import com.ticketShop.dto.auth.RefreshTokenRequest;
import com.ticketShop.mapper.UserControllerMapper;
import com.ticketShop.model.command.auth.RegisterUserCommand;
import com.ticketShop.model.dto.AuthResponse;
import com.ticketShop.model.dto.UserDTO;
import com.ticketShop.model.enums.ResultUtil;
import com.ticketShop.model.vo.ResultMessage;
import com.ticketShop.service.user.UserAppService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private UserAppService userAppService;

    @PostMapping("/register")
    @Operation(summary = "Đăng ký tài khoản mới")
    public ResultMessage<?> register(@Valid @RequestBody UserRegistrationRequest request) {
        log.info("API Request - Register user: {}", request.getUsername());
        RegisterUserCommand command = UserControllerMapper.toCommand(request);
        userAppService.register(command);
        return ResultUtil.success("Đăng ký tài khoản thành công");
    }

    @PostMapping("/login")
    @Operation(summary = "Đăng nhập hệ thống lấy token")
    public ResultMessage<AuthResponse> login(@Valid @RequestBody UserLoginRequest request) {
        log.info("API Request - Login user: {}", request.getUsername());
        AuthResponse response = userAppService.login(request.getUsername(), request.getPassword());
        return ResultUtil.data(response);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Làm mới Access Token")
    public ResultMessage<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        log.info("API Request - Refresh token");
        AuthResponse response = userAppService.refreshToken(request.getRefreshToken());
        return ResultUtil.data(response);
    }
}