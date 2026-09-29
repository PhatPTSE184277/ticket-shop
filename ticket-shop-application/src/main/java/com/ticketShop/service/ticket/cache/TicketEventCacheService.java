package com.ticketShop.service.ticket.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.ticketShop.cache.RedisInfraService;
import com.ticketShop.distributed.redisson.RedisDistributedLocker;
import com.ticketShop.distributed.redisson.RedisDistributedService;
import com.ticketShop.model.entity.TicketEvent;
import com.ticketShop.service.TicketEventDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TicketEventCacheService {

    @Autowired
    private RedisDistributedService redisDistributedService;

    @Autowired
    private RedisInfraService redisInfraService;

    @Autowired
    private TicketEventDomainService ticketEventDomainService;

    /**
     * Redis key
     */
    private static final String ACTIVE_EVENTS_KEY = "PRO_TICKET:EVENT:ACTIVE";
    private static final String ACTIVE_EVENTS_LOCK_KEY = "PRO_LOCK_KEY_EVENT:ACTIVE";

    //guava
//    private static final Cache<String, List<TicketEvent>> localCache = CacheBuilder.newBuilder()
//            .initialCapacity(1)
//            .concurrencyLevel(4)
//            .expireAfterWrite(5, TimeUnit.MINUTES)
//            .build();

    //caffein
    private static final Cache<String, List<TicketEvent>> localCache = Caffeine.newBuilder()
            .initialCapacity(1)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    public List<TicketEvent> getActiveTicketEvents() {

        // 1. Local Cache
        List<TicketEvent> ticketEvents =
                localCache.getIfPresent(ACTIVE_EVENTS_KEY);

        if (ticketEvents != null && !ticketEvents.isEmpty()) {
//            log.info("GET ACTIVE EVENTS FROM LOCAL CACHE");
            return ticketEvents;
        }

        // 2. Redis
        return getActiveTicketEventsDistributedCache();
    }

    private List<TicketEvent> getActiveTicketEventsDistributedCache() {
        List<TicketEvent> ticketEvents = redisInfraService.getList(ACTIVE_EVENTS_KEY, TicketEvent.class);

        if (ticketEvents == null || ticketEvents.isEmpty()) {;
            ticketEvents = getActiveTicketEventsDatabase();
        }

        if (ticketEvents != null && !ticketEvents.isEmpty()) {
            log.info("GET ACTIVE EVENTS FROM DISTRIBUTED CACHE");
            localCache.put(ACTIVE_EVENTS_KEY, ticketEvents);
        }

        return ticketEvents;
    }

    private List<TicketEvent> getActiveTicketEventsDatabase() {
        RedisDistributedLocker locker = redisDistributedService.getDistributedLock(ACTIVE_EVENTS_LOCK_KEY);
        boolean isLock = false;
        try {
            isLock = locker.tryLock(3, 5, TimeUnit.SECONDS);
            if (!isLock) {
                log.warn("FAILED TO ACQUIRE ACTIVE EVENTS LOCK");

                List<TicketEvent> ticketEvents =
                        redisInfraService.getList(
                                ACTIVE_EVENTS_KEY,
                                TicketEvent.class
                        );

                if (ticketEvents != null && !ticketEvents.isEmpty()) {
                    return ticketEvents;
                }

                return null;
            }

            // Double check Redis
            List<TicketEvent> ticketEvents = redisInfraService.getList(ACTIVE_EVENTS_KEY, TicketEvent.class);

            if (ticketEvents != null && !ticketEvents.isEmpty()) {
                log.info("GET ACTIVE EVENTS FROM DISTRIBUTED CACHE");
                return ticketEvents;
            }

            //Database
            ticketEvents = ticketEventDomainService.getAllActiveTicketEvents();
            log.info("GET ACTIVE EVENTS FROM DATABASE");
            if (ticketEvents == null || ticketEvents.isEmpty()) {
                return null;
            }

            // Redis
            redisInfraService.setObject(ACTIVE_EVENTS_KEY, ticketEvents);
            return ticketEvents;
        } catch (Exception e) {
            log.error(
                    "GET ACTIVE EVENTS FROM DATABASE ERROR",
                    e
            );

            throw new RuntimeException(e);
        } finally {
            if (isLock) {
                locker.unlock();
            }
        }
    }

    public void invalidateActiveTicketEvents(){
        localCache.invalidate(ACTIVE_EVENTS_KEY);
        redisInfraService.delete(ACTIVE_EVENTS_KEY);
        log.info("INVALIDATE ACTIVE EVENTS CACHE");
    }
}
