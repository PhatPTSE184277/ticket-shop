package com.ticketShop.service.ticket.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ticketShop.cache.RedisInfraService;
import com.ticketShop.distributed.redisson.RedisDistributedLocker;
import com.ticketShop.distributed.redisson.RedisDistributedService;
import com.ticketShop.model.cache.TicketCache;
import com.ticketShop.model.entity.Ticket;
import com.ticketShop.service.TicketDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TicketCacheService {
    @Autowired
    private RedisDistributedService redisDistributedService;

    @Autowired
    private RedisInfraService redisInfraService;

    @Autowired
    private TicketDomainService ticketDomainService;

    // private static final Logger log = LoggerFactory.getLogger(TicketItemCacheService.class);
    // use guava
    private final static Cache<Long, TicketCache> ticketLocalCache = CacheBuilder.newBuilder()
            .initialCapacity(10)
            .concurrencyLevel(12)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    /** * Local Cache * * key = eventId * value = EventCache */
    private final static Cache<Long, List<TicketCache>> ticketsByEventLocalCache = CacheBuilder.newBuilder()
            .initialCapacity(10)
            .concurrencyLevel(12)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    /**
     * get ticket by id in cache
     */
    public TicketCache getTicket(Long ticketId, Long version) {
        // 1 - get data from local cache
        TicketCache ticketCache = getTicketLocalCache(ticketId);

        if (ticketCache != null) {

            // User:version, cache:version
            // 1. version = null
            if (version == null){
                log.info("01: GET TICKET FROM LOCAL CACHE: versionUser:{}, versionLocal: {}", version, ticketCache.getVersion());
                return ticketCache;
            }

            if (version.equals(ticketCache.getVersion())){
                log.info("02: GET TICKET FROM LOCAL CACHE: versionUser:{}, versionLocal: {}", version, ticketCache.getVersion());
                return ticketCache;
            }

            // version < ticketCache.getVersion()
            if (version < ticketCache.getVersion()){
                log.info("03: GET TICKET FROM LOCAL CACHE: versionUser:{}, versionLocal: {}", version, ticketCache.getVersion());
                return ticketCache;
            }

            if (version > ticketCache.getVersion()){
                return getTicketDistributedCache(ticketId);
            }
//            return ticketCache;
        }
        return getTicketDistributedCache(ticketId);
    }

    /**
     * get ticket from database
     */
    public TicketCache getTicketDatabase(Long id) {
        RedisDistributedLocker locker = redisDistributedService.getDistributedLock(genTicketKeyLock(id));
        boolean isLock = false;
        try {
            // 1 - Tao lock
            isLock = locker.tryLock(1, 5, TimeUnit.SECONDS);
            // Lưu ý: Cho dù thành công hay không cũng phải unLock, bằng mọi giá.
            if (!isLock) {
                return null; //return retry
            }

            // Get cache
            TicketCache ticketCache = redisInfraService.getObject(genTicketKey(id), TicketCache.class);
            //2. YES
            if (ticketCache != null) {
                return ticketCache;
            }
            Ticket ticket = ticketDomainService.getTicketById(id);
            if (ticket == null) {
                return null;
            }
            ticketCache = new TicketCache().withClone(ticket).withVersion(System.currentTimeMillis());
            // set data to distributed cache
            redisInfraService.setObject(genTicketKey(id), ticketCache);
            return ticketCache;

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (isLock) {
                locker.unlock();
            }
        }
    }

    /**
     * get ticket from distributed cache
     */
    public TicketCache getTicketDistributedCache(Long id){
        //1 - Get Data
        TicketCache ticketCache = redisInfraService.getObject(genTicketKey(id), TicketCache.class);
        if(ticketCache == null){
            log.info("GET TICKET FROM DISTRIBUTED LOCK");
            ticketCache = getTicketDatabase(id);
        }
        // 2 - put data to local cache
        // lock()
        if(ticketCache != null) {
            ticketLocalCache.put(id, ticketCache); //.. consistency cache
        }
        // unLock()
        log.info("GET TICKET FROM DISTRIBUTED CACHE");
        return ticketCache;
    }

    /**
     * get ticket from local cache
     */
    public TicketCache getTicketLocalCache(Long id){
        //get cache from GUAVA
        return  ticketLocalCache.getIfPresent(id);
    }


    // ============================================================
    // GET TICKETS BY EVENT

    /**
     * get tickets by event id in cache
     */
    public List<TicketCache> getTicketsByEvent(Long eventId){
        // 1 - get data from local cache
        List<TicketCache> ticketCaches = ticketsByEventLocalCache.getIfPresent(eventId);

        if (ticketCaches != null && !ticketCaches.isEmpty()){
            log.info(
                    "GET TICKETS FROM LOCAL CACHE - eventId: {}",
                    eventId
            );

            return ticketCaches;
        }

        // 2 - get data from distributed cache
        return getTicketsByEventDistributedCache(eventId);
    }

    /**
     * get tickets from distributed cache
     */
    public List<TicketCache> getTicketsByEventDistributedCache(Long eventId){
        // 1 - Get Data from Redis
        List<TicketCache> ticketCaches = redisInfraService.getList(genEventListKey(eventId), TicketCache.class);

        if (ticketCaches == null || ticketCaches.isEmpty()) {
              log.info(
                "GET TICKETS FROM DISTRIBUTED LOCK - eventId: {}",
                eventId
                );
              ticketCaches = getTicketByEventDatabase(eventId);
        }

        // 2 - put data to local cache
        if (ticketCaches != null && !ticketCaches.isEmpty()) {
            ticketsByEventLocalCache.put(eventId, ticketCaches);
        }

        log.info(
                "GET TICKETS FROM DISTRIBUTED CACHE - eventId: {}",
                eventId
        );

        return ticketCaches;
    }

    /**
     * get ticket from database
     */
    public List<TicketCache> getTicketByEventDatabase(Long eventId) {
        RedisDistributedLocker locker = redisDistributedService.getDistributedLock(genEventKeyLock(eventId));
        boolean isLock = false;
        try {
            isLock = locker.tryLock(1, 5, TimeUnit.SECONDS);
            if (!isLock) {
                return null;
            }

            List<TicketCache> ticketCaches = redisInfraService.getList(genEventListKey(eventId), TicketCache.class);
            if (ticketCaches != null && !ticketCaches.isEmpty()) {
                return ticketCaches;
            }

            List<Ticket> tickets = ticketDomainService.getTicketsByEventId(eventId);
            if (tickets == null || tickets.isEmpty()) {
                return null;
            }

            ticketCaches = tickets.stream()
                    .map(ticket -> new TicketCache()
                            .withClone(ticket)
                            .withVersion(ticket.getVersion())
                    )
                    .toList();

            redisInfraService.setObject(genEventListKey(eventId), ticketCaches);
            return ticketCaches;
        } catch (Exception e) {
            log.error(
                    "GET TICKETS FROM DATABASE ERROR - eventId: {}",
                    eventId,
                    e
            );
            throw new RuntimeException(e);
        } finally {
            if (isLock) {
                locker.unlock();
            }
        }
    }



    /**
     * invalidate ticket cache
     */
    public void invalidTicketCache(Long id){
        ticketLocalCache.invalidate(id);
        redisInfraService.delete(genTicketKey(id));
    }

    /**
     * invalidate tickets by event cache
     */
    public void invalidTicketsByEventCache(Long eventId){
        ticketsByEventLocalCache.invalidate(eventId);
        redisInfraService.delete(genEventListKey(eventId));
    }


    /**
     * Redis key
     */

    //Item
    private String genTicketKey(Long id){
        return "PRO_TICKET:" + id;
    }
    private String genTicketKeyLock(Long id){
        return "PRO_LOCK_KEY_TICKET:" + id;
    }

    //Event
    private String genEventListKey(Long eventId) {
        return "PRO_EVENT:" + eventId;
    }
    private String genEventKeyLock(Long eventId){
        return "PRO_LOCK_KEY_EVENT:" + eventId;
    }
}
