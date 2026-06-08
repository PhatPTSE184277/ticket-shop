package com.xxxx.ddd.infrastructure.cache.redis;

import java.util.concurrent.TimeUnit;

public interface RedisInfrasService {
    void setString(String key, String value);
    String getString(String key);

    void setObject(String key, Object value);
    void setObject(String key, Object value, long timeout, TimeUnit timeUnit);
    <T> T getObject(String key, Class<T> targetClass);

//    void put(String key, Object value, long timeOut, TimeUnit timeUnit);
//    void put(String key, Object value, long expireTime);

    //delete redis by key
    void delete(String key);
}
