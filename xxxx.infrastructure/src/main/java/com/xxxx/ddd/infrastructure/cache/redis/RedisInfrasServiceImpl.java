package com.xxxx.ddd.infrastructure.cache.redis;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component
@Slf4j
public class RedisInfrasServiceImpl implements RedisInfrasService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void setString(String key, String value) {
        if (StringUtils.hasLength(key)){ //null or ''
            return;
        }
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String getString(String key) {
//        Object result = redisTemplate.opsForValue().get(key);
//        if (result == null) {
//            return null;
//        }
//        return String.valueOf(result);
        return Optional.ofNullable(redisTemplate.opsForValue().get(key))
                .map(String::valueOf)
                .orElse(null);
    }

    @Override
    public void setObject(String key, Object value) {
//        log.info("Set redis::1, {}", key);
        if (!StringUtils.hasLength(key)){
            //log.info("Set redis::null, {}", StringUtils.hasLength(key));
            return;
        }

        try{
            redisTemplate.opsForValue().set(key, value);
        }catch (Exception e){
            log.error("setObject error:{}", e.getMessage());
        }
//        redisTemplate.opsForValue().set(key, value);
//        // Kiểm tra xem giá trị có được lưu thành công hay không
//        Object result = redisTemplate.opsForValue().get(key);
//        log.info("Set redis::{}", result != null && result.equals(value));
    }

    @Override
    public <T> T getObject(String key, Class<T> targetClass) {
        Object result = redisTemplate.opsForValue().get(key);
        log.info("getCache::{}", result);
        if (result == null){
            return null;
        }
        return null;
    }
}
