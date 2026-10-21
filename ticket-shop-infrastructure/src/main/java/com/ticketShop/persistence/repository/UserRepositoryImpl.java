package com.ticketShop.persistence.repository;

import com.ticketShop.model.entity.User;
import com.ticketShop.model.enums.UserStatus;
import com.ticketShop.persistence.mapper.UserJPAMapper;
import com.ticketShop.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private UserJPAMapper userJPAMapper;

    @Override
    public Optional<User> findById(Long id) {
        return userJPAMapper.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJPAMapper.findByUsername(username);
    }

    @Override
    public User save(User user) {
        log.info("Saving user: {}", user.getUsername());
        return userJPAMapper.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJPAMapper.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJPAMapper.existsByEmail(email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return userJPAMapper.existsByPhone(phone);
    }

    @Override
    public Page<User> findByStatus(UserStatus status, Pageable pageable) {
        if (status == null) {
            return userJPAMapper.findAll(pageable);
        }
        return userJPAMapper.findByStatus(status, pageable);
    }
}