package com.ticketShop.distributed.redisson;

public interface RedisDistributedService {
    RedisDistributedLocker getDistributedLock(String lockKey);
}
