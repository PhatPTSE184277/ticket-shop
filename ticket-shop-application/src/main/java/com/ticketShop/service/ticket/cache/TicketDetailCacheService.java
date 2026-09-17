package com.ticketShop.service.ticket.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ticketShop.cache.RedisInfraService;
import com.ticketShop.distributed.redisson.RedisDistributedLocker;
import com.ticketShop.distributed.redisson.RedisDistributedService;
import com.ticketShop.model.cache.TicketDetailCache;
import com.ticketShop.model.entity.TicketDetail;
import com.ticketShop.service.TicketDetailDomainService;
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
    private TicketDetailDomainService ticketDetailDomainService;

    // private static final Logger log = LoggerFactory.getLogger(TicketDetailCacheService.class);
    // use guava
    private final static Cache<Long, TicketDetailCache> ticketDetailLocalCache = CacheBuilder.newBuilder()
            .initialCapacity(10)
            .concurrencyLevel(12)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    /**
     * get ticket item by id in cache
     */
    public TicketDetailCache getTicketDetail(Long ticketId, Long version) {
        // 1 - get data from local cache
        TicketDetailCache ticketDetailCache = getTicketDetailLocalCache(ticketId);

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
    public TicketDetailCache getTicketDetailDatabase(Long id){
        RedisDistributedLocker locker = redisDistributedService.getDistributedLock(genEventItemKetLock(id));
        try {
            // 1 - Tao lock
            boolean isLock = locker.tryLock(1, 5, TimeUnit.SECONDS);
            // Lưu ý: Cho dù thành công hay không cũng phải unLock, bằng mọi giá.
            if(!isLock){
                return null; //return retry
            }

            // Get cache
            TicketDetailCache ticketDetailCache = redisInfraService.getObject(genEventItemKey(id), TicketDetailCache.class);
            //2. YES
            if (ticketDetailCache != null){
                return ticketDetailCache;
            }
            TicketDetail ticketDetail = ticketDetailDomainService.getTicketDetailById(id);
            ticketDetailCache = new TicketDetailCache().withClone(ticketDetail).withVersion(System.currentTimeMillis());
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
    public TicketDetailCache getTicketDetailDistributedCache(Long id){
        //1 - Get Data
        TicketDetailCache ticketDetailCache = redisInfraService.getObject(genEventItemKey(id), TicketDetailCache.class);
        if(ticketDetailCache == null){
            log.info("GET TICKET FROM DISTRIBUTED LOCK");
            ticketDetailCache = getTicketDetailDatabase(id);
        }
        // 2 - put data to local cache
        // lock()
        ticketDetailLocalCache.put(id, ticketDetailCache); //.. consistency cache
        // unLock()
        log.info("GET TICKET FROM DISTRIBUTED CACHE");
        return ticketDetailCache;
    }

    /**
     * get ticket from local cache
     */
    public TicketDetailCache getTicketDetailLocalCache(Long id){
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
