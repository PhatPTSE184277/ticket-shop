package com.ticketShop.service.user.impl;

import com.ticketShop.exception.ServiceException;
import com.ticketShop.exception.enums.ResultCode;
import com.ticketShop.mapper.UserMapper;
import com.ticketShop.model.command.auth.RegisterUserCommand;
import com.ticketShop.model.dto.AuthResponse;
import com.ticketShop.model.dto.UserDTO;
import com.ticketShop.model.entity.User;
import com.ticketShop.model.enums.UserStatus;
import com.ticketShop.repository.UserRepository;
import com.ticketShop.security.JwtTokenProvider;
import com.ticketShop.service.user.UserAppService;
import com.ticketShop.validator.UserValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserAppServiceImpl implements UserAppService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public UserDTO register(RegisterUserCommand command) {
        log.info("App Service - Registering user: {}", command.getUsername());
        String encodedPassword = passwordEncoder.encode(command.getPassword());

        User user = userMapper.toEntity(command, encodedPassword);

        userValidator.validateUserRegistration(user);


        User savedUser = userRepository.save(user);
        log.info("Registered user successfully with ID: {}", savedUser.getId());

        return userMapper.toDTO(savedUser);
    }

    @Override
    public AuthResponse login(String username, String password) {
        log.info("App Service - User logging in: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ServiceException(ResultCode.USER_NOT_EXIST));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ServiceException(ResultCode.PASSWORD_ERROR);
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new ServiceException(ResultCode.ACCOUNT_LOCKED);
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getUsername(), user.getRole().name());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        return new AuthResponse(accessToken, refreshToken, jwtTokenProvider.getExpirationMs());
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new ServiceException(ResultCode.TOKEN_INVALID);
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ResultCode.USER_NOT_EXIST));

        String newAccessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getUsername(), user.getRole().name());
        return new AuthResponse(newAccessToken, refreshToken, jwtTokenProvider.getExpirationMs());
    }

    @Override
    public UserDTO getCurrentUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ResultCode.USER_NOT_EXIST));
        return userMapper.toDTO(user);
    }

    @Override
    public Page<UserDTO> getUsersByStatus(UserStatus status, Pageable pageable) {
        log.info("App Service - Get users by status: {}, page: {}, size: {}",
                status, pageable.getPageNumber(), pageable.getPageSize());
        Page<User> userPage = userRepository.findByStatus(status, pageable);
        return userPage.map(userMapper::toDTO);
    }
}