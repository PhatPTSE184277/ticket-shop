package com.ticketShop.service.ticket.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.ticketShop.cache.RedisInfraService;
import com.ticketShop.distributed.redisson.RedisDistributedLocker;
import com.ticketShop.distributed.redisson.RedisDistributedService;
import com.ticketShop.model.entity.Event;
import com.ticketShop.service.EventDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class EventCacheService {

    @Autowired
    private RedisDistributedService redisDistributedService;

    @Autowired
    private RedisInfraService redisInfraService;

    @Autowired
    private EventDomainService eventDomainService;

    /**
     * Redis key
     */
    private static final String ACTIVE_EVENTS_KEY = "PRO_EVENT:ACTIVE";
    private static final String ACTIVE_EVENTS_LOCK_KEY = "PRO_LOCK_KEY_EVENT:ACTIVE";

    //guava
//    private static final Cache<String, List<TicketEvent>> localCache = CacheBuilder.newBuilder()
//            .initialCapacity(1)
//            .concurrencyLevel(4)
//            .expireAfterWrite(5, TimeUnit.MINUTES)
//            .build();

    //caffein
    private static final Cache<String, List<Event>> localCache = Caffeine.newBuilder()
            .initialCapacity(1)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    public List<Event> getActiveEvents() {

        // 1. Local Cache
        List<Event> events =
                localCache.getIfPresent(ACTIVE_EVENTS_KEY);

        if (events != null && !events.isEmpty()) {
//            log.info("GET ACTIVE EVENTS FROM LOCAL CACHE");
            return events;
        }

        // 2. Redis
        return getActiveEventsDistributedCache();
    }

    private List<Event> getActiveEventsDistributedCache() {
        List<Event> events = redisInfraService.getList(ACTIVE_EVENTS_KEY, Event.class);

        if (events == null || events.isEmpty()) {;
            events = getActiveEventsDatabase();
        }

        if (events != null && !events.isEmpty()) {
            log.info("GET ACTIVE EVENTS FROM DISTRIBUTED CACHE");
            localCache.put(ACTIVE_EVENTS_KEY, events);
        }

        return events;
    }

    private List<Event> getActiveEventsDatabase() {
        RedisDistributedLocker locker = redisDistributedService.getDistributedLock(ACTIVE_EVENTS_LOCK_KEY);
        boolean isLock = false;
        try {
            isLock = locker.tryLock(3, 5, TimeUnit.SECONDS);
            if (!isLock) {
                log.warn("FAILED TO ACQUIRE ACTIVE EVENTS LOCK");

                List<Event> events =
                        redisInfraService.getList(
                                ACTIVE_EVENTS_KEY,
                                Event.class
                        );

                if (events != null && !events.isEmpty()) {
                    return events;
                }

                return null;
            }

            // Double check Redis
            List<Event> events = redisInfraService.getList(ACTIVE_EVENTS_KEY, Event.class);

            if (events != null && !events.isEmpty()) {
                log.info("GET ACTIVE EVENTS FROM DISTRIBUTED CACHE");
                return events;
            }

            //Database
            events = eventDomainService.getAllActiveEvents();
            log.info("GET ACTIVE EVENTS FROM DATABASE");
            if (events == null || events.isEmpty()) {
                return null;
            }

            // Redis
            redisInfraService.setObject(ACTIVE_EVENTS_KEY, events);
            return events;
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

    public void invalidateActiveEvents(){
        localCache.invalidate(ACTIVE_EVENTS_KEY);
        redisInfraService.delete(ACTIVE_EVENTS_KEY);
        log.info("INVALIDATE ACTIVE EVENTS CACHE");
    }
}
