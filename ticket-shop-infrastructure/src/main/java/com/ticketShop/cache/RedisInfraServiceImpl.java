package com.ticketShop.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.mapping.Map;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component
@Slf4j
public class RedisInfraServiceImpl implements RedisInfraService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void setString(String key, String value) {
        if (!StringUtils.hasLength(key)){ //null or ''
            return;
        }
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String getString(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key))
                .map(String::valueOf)
                .orElse(null);
    }

    @Override
    public void setObject(String key, Object value) {
        if (!StringUtils.hasLength(key)){
            return;
        }
        try {
            redisTemplate.opsForValue().set(key, value);
        }catch (Exception e){
            log.error("setObject error: key={}, error={}", key, e.getMessage(), e);
        }
    }

    @Override
    public <T> T getObject(String key, Class<T> targetClass) {
        Object result = redisTemplate.opsForValue().get(key);
        if (result == null){
            return null;
        }

        // Nếu kết quả là một LinkedHashMap
        if(result instanceof Map){
            try {
                // Chuyển đổi LinkedHashMap thành đối tượng mục tiêu
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.convertValue(result, targetClass);
            }catch (IllegalArgumentException e){
                return null;
            }
        }

        // Nếu result là String, thực hiện chuyển đổi bình thường
        if (result instanceof String){
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue((String) result, targetClass);
            }catch (JsonProcessingException e){
                return null;
            }
        }
        return null;
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    @Override
    public void setInt(String key, int value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public int getInt(String key) {
        Object result = redisTemplate.opsForValue().get(key);
        if (result == null) {
            return 0;
        }
        return Integer.parseInt(result.toString());
    }
}
