package com.ticketShop.service.ticket.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ticketShop.cache.RedisInfraService;
import com.ticketShop.distributed.redisson.RedisDistributedLocker;
import com.ticketShop.distributed.redisson.RedisDistributedService;
import com.ticketShop.model.cache.TicketItemCache;
import com.ticketShop.model.entity.TicketItem;
import com.ticketShop.service.TicketItemDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TicketItemCacheService {
    @Autowired
    private RedisDistributedService redisDistributedService;

    @Autowired
    private RedisInfraService redisInfraService;

    @Autowired
    private TicketItemDomainService ticketItemDomainService;

    // private static final Logger log = LoggerFactory.getLogger(TicketItemCacheService.class);
    // use guava
    private final static Cache<Long, TicketItemCache> ticketItemLocalCache = CacheBuilder.newBuilder()
            .initialCapacity(10)
            .concurrencyLevel(12)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    /** * Local Cache * * key = eventId * value = TicketEventCache */
    private final static Cache<Long, List<TicketItemCache>> ticketItemsByEventLocalCache = CacheBuilder.newBuilder()
            .initialCapacity(10)
            .concurrencyLevel(12)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    /**
     * get ticket item by id in cache
     */
    public TicketItemCache getTicketItem(Long ticketId, Long version) {
        // 1 - get data from local cache
        TicketItemCache ticketItemCache = getTicketItemLocalCache(ticketId);

        if (ticketItemCache != null) {

            // User:version, cache:version
            // 1. version = null
            if (version == null){
                log.info("01: GET TICKET FROM LOCAL CACHE: versionUser:{}, versionLocal: {}", version, ticketItemCache.getVersion());
                return ticketItemCache;
            }

            if (version.equals(ticketItemCache.getVersion())){
                log.info("02: GET TICKET FROM LOCAL CACHE: versionUser:{}, versionLocal: {}", version, ticketItemCache.getVersion());
                return ticketItemCache;
            }

            // version < ticketItemCache.getVersion()
            if (version < ticketItemCache.getVersion()){
                log.info("03: GET TICKET FROM LOCAL CACHE: versionUser:{}, versionLocal: {}", version, ticketItemCache.getVersion());
                return ticketItemCache;
            }

            if (version > ticketItemCache.getVersion()){
                return getTicketItemDistributedCache(ticketId);
            }
//            return ticketItemCache;
        }
        return getTicketItemDistributedCache(ticketId);
    }

    /**
     * get ticket from database
     */
    public TicketItemCache getTicketItemDatabase(Long id) {
        RedisDistributedLocker locker = redisDistributedService.getDistributedLock(genEventItemKeyLock(id));
        boolean isLock = false;
        try {
            // 1 - Tao lock
            isLock = locker.tryLock(1, 5, TimeUnit.SECONDS);
            // Lưu ý: Cho dù thành công hay không cũng phải unLock, bằng mọi giá.
            if (!isLock) {
                return null; //return retry
            }

            // Get cache
            TicketItemCache ticketItemCache = redisInfraService.getObject(genEventItemKey(id), TicketItemCache.class);
            //2. YES
            if (ticketItemCache != null) {
                return ticketItemCache;
            }
            TicketItem ticketItem = ticketItemDomainService.getTicketItemById(id);
            if (ticketItem == null) {
                return null;
            }
            ticketItemCache = new TicketItemCache().withClone(ticketItem).withVersion(System.currentTimeMillis());
            // set data to distributed cache
            redisInfraService.setObject(genEventItemKey(id), ticketItemCache);
            return ticketItemCache;

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
    public TicketItemCache getTicketItemDistributedCache(Long id){
        //1 - Get Data
        TicketItemCache ticketItemCache = redisInfraService.getObject(genEventItemKey(id), TicketItemCache.class);
        if(ticketItemCache == null){
            log.info("GET TICKET FROM DISTRIBUTED LOCK");
            ticketItemCache = getTicketItemDatabase(id);
        }
        // 2 - put data to local cache
        // lock()
        if(ticketItemCache != null) {
            ticketItemLocalCache.put(id, ticketItemCache); //.. consistency cache
        }
        // unLock()
        log.info("GET TICKET FROM DISTRIBUTED CACHE");
        return ticketItemCache;
    }

    /**
     * get ticket from local cache
     */
    public TicketItemCache getTicketItemLocalCache(Long id){
        //get cache from GUAVA
        return  ticketItemLocalCache.getIfPresent(id);
    }


    // ============================================================
    // GET TICKET ITEMS BY EVENT

    /**
     * get ticket items by event id in cache
     */
    public List<TicketItemCache> getTicketItemsByEvent(Long eventId){
        // 1 - get data from local cache
        List<TicketItemCache> ticketItemCaches = ticketItemsByEventLocalCache.getIfPresent(eventId);

        if (ticketItemCaches != null && !ticketItemCaches.isEmpty()){
            log.info(
                    "GET TICKET ITEMS FROM LOCAL CACHE - eventId: {}",
                    eventId
            );

            return ticketItemCaches;
        }

        // 2 - get data from distributed cache
        return getTicketItemsByEventDistributedCache(eventId);
    }

    /**
     * get ticket items from distributed cache
     */
    public List<TicketItemCache> getTicketItemsByEventDistributedCache(Long eventId){
        // 1 - Get Data from Redis
        List<TicketItemCache> ticketItemCaches = redisInfraService.getList(genEventListKey(eventId), TicketItemCache.class);

        if (ticketItemCaches == null || ticketItemCaches.isEmpty()) {
              log.info(
                "GET TICKET ITEMS FROM DISTRIBUTED LOCK - eventId: {}",
                eventId
                );
              ticketItemCaches = getTicketItemsByEventDatabase(eventId);
        }

        // 2 - put data to local cache
        if (ticketItemCaches != null && !ticketItemCaches.isEmpty()) {
            ticketItemsByEventLocalCache.put(eventId, ticketItemCaches);
        }

        log.info(
                "GET TICKET ITEMS FROM DISTRIBUTED CACHE - eventId: {}",
                eventId
        );

        return ticketItemCaches;
    }

    /**
     * get ticket items from database
     */
    public List<TicketItemCache> getTicketItemsByEventDatabase(Long eventId) {
        RedisDistributedLocker locker = redisDistributedService.getDistributedLock(genEventKeyLock(eventId));
        boolean isLock = false;
        try {
            isLock = locker.tryLock(1, 5, TimeUnit.SECONDS);
            if (!isLock) {
                return null;
            }

            List<TicketItemCache> ticketItemCaches = redisInfraService.getList(genEventListKey(eventId), TicketItemCache.class);
            if (ticketItemCaches != null && !ticketItemCaches.isEmpty()) {
                return ticketItemCaches;
            }

            List<TicketItem> ticketItems = ticketItemDomainService.getTicketItemsByEventId(eventId);
            if (ticketItems == null || ticketItems.isEmpty()) {
                return null;
            }

            ticketItemCaches = ticketItems.stream()
                    .map(ticketItem -> new TicketItemCache()
                            .withClone(ticketItem)
                            .withVersion(ticketItem.getVersion())
                    )
                    .toList();

            redisInfraService.setObject(genEventListKey(eventId), ticketItemCaches);
            return ticketItemCaches;
        } catch (Exception e) {
            log.error(
                    "GET TICKET ITEMS FROM DATABASE ERROR - eventId: {}",
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
     * invalidate ticket item cache
     */
    public void invalidTicketItemCache(Long id){
        ticketItemLocalCache.invalidate(id);
        redisInfraService.delete(genEventItemKey(id));
    }

    /**
     * invalidate ticket items by event cache
     */
    public void invalidTicketItemsByEventCache(Long eventId){
        ticketItemsByEventLocalCache.invalidate(eventId);
        redisInfraService.delete(genEventListKey(eventId));
    }


    /**
     * Redis key
     */

    //Item
    private String genEventItemKey(Long id){
        return "PRO_TICKET:ITEM:" + id;
    }
    private String genEventItemKeyLock(Long id){
        return "PRO_LOCK_KEY_ITEM:" + id;
    }

    //Event Item
    private String genEventListKey(Long eventId) {
        return "PRO_TICKET:EVENT_ITEMS:" + eventId;
    }
    private String genEventKeyLock(Long eventId){
        return "PRO_LOCK_KEY_EVENT:" + eventId;
    }
}
