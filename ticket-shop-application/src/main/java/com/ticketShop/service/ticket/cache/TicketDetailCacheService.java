package com.ticketShop.service.ticket.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ticketShop.cache.RedisInfraService;
import com.ticketShop.distributed.redisson.RedisDistributedLocker;
import com.ticketShop.model.cache.TicketItemCache;
import com.ticketShop.model.entity.TicketItem;
import com.ticketShop.service.TicketItemDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TicketDetailCacheService {
    @Autowired
    private com.ticketShop.distributed.redisson.RedisDistributedService redisDistributedService;

    @Autowired
    private RedisInfraService redisInfraService;

    @Autowired
    private TicketItemDomainService ticketDetailDomainService;

    // private static final Logger log = LoggerFactory.getLogger(TicketDetailCacheService.class);
    // use guava
    private final static Cache<Long, TicketItemCache> ticketDetailLocalCache = CacheBuilder.newBuilder()
            .initialCapacity(10)
            .concurrencyLevel(12)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    /**
     * get ticket item by id in cache
     */
    public TicketItemCache getTicketDetail(Long ticketId, Long version) {
        // 1 - get data from local cache
        TicketItemCache ticketDetailCache = getTicketDetailLocalCache(ticketId);

        if (ticketDetailCache != null) {

            // User:version, cache:version
            // 1. version = null
            if (version == null){
                log.info("01: GET TICKET FROM LOCAL CACHE: versionUser:{}, versionLocal: {}", version, ticketDetailCache.getVersion());
                return ticketDetailCache;
            }

            if (version.equals(ticketDetailCache.getVersion())){
                log.info("02: GET TICKET FROM LOCAL CACHE: versionUser:{}, versionLocal: {}", version, ticketDetailCache.getVersion());
                return ticketDetailCache;
            }

            // version < ticketDetailCache.getVersion()
            if (version < ticketDetailCache.getVersion()){
                log.info("03: GET TICKET FROM LOCAL CACHE: versionUser:{}, versionLocal: {}", version, ticketDetailCache.getVersion());
                return ticketDetailCache;
            }

            if (version > ticketDetailCache.getVersion()){
                return getTicketDetailDistributedCache(ticketId);
            }
//            return ticketDetailCache;
        }
        return getTicketDetailDistributedCache(ticketId);
    }

    /**
     * get ticket from database
     */
    public TicketItemCache getTicketDetailDatabase(Long id){
        RedisDistributedLocker locker = redisDistributedService.getDistributedLock(genEventItemKetLock(id));
        try {
            // 1 - Tao lock
            boolean isLock = locker.tryLock(1, 5, TimeUnit.SECONDS);
            // Lưu ý: Cho dù thành công hay không cũng phải unLock, bằng mọi giá.
            if(!isLock){
                return null; //return retry
            }

            // Get cache
            TicketItemCache ticketDetailCache = redisInfraService.getObject(genEventItemKey(id), TicketItemCache.class);
            //2. YES
            if (ticketDetailCache != null){
                return ticketDetailCache;
            }
            TicketItem ticketDetail = ticketDetailDomainService.getTicketItemById(id);
            if (ticketDetail == null) {
                return null;
            }
            ticketDetailCache = new TicketItemCache().withClone(ticketDetail).withVersion(System.currentTimeMillis());
            // set data to distributed cache
            redisInfraService.setObject(genEventItemKey(id), ticketDetailCache);
            return ticketDetailCache;

        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            locker.unlock();
        }
    }

    /**
     * get ticket from distributed cache
     */
    public TicketItemCache getTicketDetailDistributedCache(Long id){
        //1 - Get Data
        TicketItemCache ticketDetailCache = redisInfraService.getObject(genEventItemKey(id), TicketItemCache.class);
        if(ticketDetailCache == null){
            log.info("GET TICKET FROM DISTRIBUTED LOCK");
            ticketDetailCache = getTicketDetailDatabase(id);
        }
        // 2 - put data to local cache
        // lock()
        if(ticketDetailCache != null) {
            ticketDetailLocalCache.put(id, ticketDetailCache); //.. consistency cache
        }
        // unLock()
        log.info("GET TICKET FROM DISTRIBUTED CACHE");
        return ticketDetailCache;
    }

    /**
     * get ticket from local cache
     */
    public TicketItemCache getTicketDetailLocalCache(Long id){
        //get cache from GUAVA
        return  ticketDetailLocalCache.getIfPresent(id);
    }


    private String genEventItemKey(Long id){
        return "PRO_TICKET:ITEM:" + id;
    }

    private String genEventItemKetLock(Long id){
        return "PRO_LOCK_KEY_ITEM:" + id;
    }
}
