package com.ticketShop.mapper;

import com.ticketShop.dto.auth.UserRegistrationRequest;
import com.ticketShop.model.command.auth.RegisterUserCommand;

public class UserControllerMapper {
    public static RegisterUserCommand toCommand(UserRegistrationRequest request) {
        if (request == null) return null;
        RegisterUserCommand command = new RegisterUserCommand();
        command.setUsername(request.getUsername());
        command.setEmail(request.getEmail());
        command.setPhone(request.getPhone());
        command.setPassword(request.getPassword());
        command.setFullName(request.getFullName());
        return command;
    }
}