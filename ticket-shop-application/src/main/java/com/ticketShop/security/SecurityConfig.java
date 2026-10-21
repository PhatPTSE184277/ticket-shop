package com.ticketShop.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Tắt CSRF vì chúng ta dùng REST API với JWT (không dùng session form)
                .csrf(csrf -> csrf.disable())

                // Cấu hình không tạo HTTP Session (Stateless) vì dùng Token
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Phân quyền đường dẫn
                .authorizeHttpRequests(auth -> auth
                        // Cho phép tất cả mọi người gọi vào các API nhóm /auth/** (đăng ký, đăng nhập, refresh-token) và Swagger
                        .requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // Các request khác tạm thời cho phép hoặc yêu cầu xác thực tùy bạn (ở đây tạm mở hoặc giữ authenticated)
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}