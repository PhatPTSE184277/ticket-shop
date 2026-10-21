package com.ticketShop.repository;

import com.ticketShop.model.entity.User;
import com.ticketShop.model.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    User save(User user);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    Page<User> findByStatus(UserStatus status, Pageable pageable);
}