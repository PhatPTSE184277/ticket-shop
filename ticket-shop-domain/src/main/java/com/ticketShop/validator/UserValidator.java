package com.ticketShop.validator;

import com.ticketShop.model.entity.User;
import com.ticketShop.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class UserValidator {

    private final UserRepository userRepository;

    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_]{3,30}$");

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^0[0-9]{9}$");

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateUserRegistration(User user) {

        if (user == null) {
            throw new IllegalArgumentException("Thông tin đăng ký không được để trống");
        }

        // 1. Validate username
        String username = user.getUsername();

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên đăng nhập không được để trống");
        }

        username = username.trim();

        if (!USERNAME_PATTERN.matcher(username).matches()) {
            throw new IllegalArgumentException(
                    "Tên đăng nhập phải từ 3 đến 30 ký tự, chỉ gồm chữ cái, số và dấu gạch dưới"
            );
        }

        // 2. Validate email
        String email = user.getEmail();

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email không được để trống");
        }

        email = email.trim();

        if (email.length() > 254
                || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Email không hợp lệ");
        }

        // 3. Validate phone
        String phone = user.getPhone();

        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống");
        }

        phone = phone.trim();

        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new IllegalArgumentException(
                    "Số điện thoại phải gồm 10 chữ số và bắt đầu bằng số 0"
            );
        }

        // 4. Validate password
        String password = user.getPassword();

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống");
        }

        if (password.length() < 8 || password.length() > 72) {
            throw new IllegalArgumentException(
                    "Mật khẩu phải có từ 8 đến 72 ký tự"
            );
        }

        if (password.chars().noneMatch(Character::isUpperCase)) {
            throw new IllegalArgumentException(
                    "Mật khẩu phải có ít nhất một chữ cái viết hoa"
            );
        }

        if (password.chars().noneMatch(Character::isLowerCase)) {
            throw new IllegalArgumentException(
                    "Mật khẩu phải có ít nhất một chữ cái viết thường"
            );
        }

        if (password.chars().noneMatch(Character::isDigit)) {
            throw new IllegalArgumentException(
                    "Mật khẩu phải có ít nhất một chữ số"
            );
        }

        // 5. Check duplicate username
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException(
                    "Tên đăng nhập đã tồn tại trên hệ thống"
            );
        }

        // 6. Check duplicate email
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email đã được sử dụng"
            );
        }

        // 7. Check duplicate phone
        if (userRepository.existsByPhone(phone)) {
            throw new IllegalArgumentException(
                    "Số điện thoại đã được sử dụng"
            );
        }
    }
}