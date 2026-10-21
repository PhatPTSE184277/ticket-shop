package com.ticketShop.model.command.auth;
import lombok.Data;

@Data
public class RegisterUserCommand {
    private String username;
    private String email;
    private String phone;
    private String password;
    private String fullName;
}