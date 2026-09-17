package com.ticketShop.distributed.redisson.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
    @Value("${REDIS_HOST:127.0.0.1}")
    private String host;

    @Value("${REDIS_PORT:6319}")
    private int port;

    @Value("${REDIS_PASSWORD:}")
    private String password;

    @Value("${REDIS_DATABASE:0}")
    private int database;

    @Value("${REDIS_CONNECTION_POOL_SIZE:50}")
    private int poolSize;

    @Value("${REDIS_CONNECTION_MINIMUM_IDLE_SIZE:10}")
    private int minIdle;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        String redisAddress = String.format("redis://%s:%d", host, port);

        var singleServerConfig = config.useSingleServer()
                .setAddress(redisAddress)
                .setDatabase(database)
                .setConnectionPoolSize(poolSize)
                .setConnectionMinimumIdleSize(minIdle);

        if (password != null && !password.isBlank()) {
            singleServerConfig.setPassword(password);
        }

        return Redisson.create(config);
    }


    // to sentinel
/*
    @Value("${REDIS_SENTINEL_MASTER_NAME:mymaster}")
    private String masterName;

    @Value("${REDIS_SENTINEL_NODES:redis://127.0.0.1:26379,redis://127.0.0.1:26380,redis://127.0.0.1:26381}")
    private String[] sentinelNodes;

    @Bean
    public RedissonClient redissonClientSentinel() {
        Config config = new Config();

        var sentinelConfig = config.useSentinelServers()
                .setMasterName(masterName)
                .addSentinelAddress(sentinelNodes)
                .setDatabase(database)
                .setCheckSentinelsList(false)
                .setMasterConnectionPoolSize(poolSize)
                .setMasterConnectionMinimumIdleSize(minIdle)
                .setSlaveConnectionPoolSize(poolSize)
                .setSlaveConnectionMinimumIdleSize(minIdle);

        if (password != null && !password.isBlank()) {
            sentinelConfig.setPassword(password);
        }

        return Redisson.create(config);
    }
    */
}
