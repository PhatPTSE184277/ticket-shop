package com.xxxx.ddd.application.service.ticket.cache;

import com.xxxx.ddd.domain.model.entity.TicketDetail;
import com.xxxx.ddd.domain.service.TicketDetailDomainService;
import com.xxxx.ddd.infrastructure.cache.redis.RedisInfrasService;
import com.xxxx.ddd.infrastructure.distributed.redisson.RedisDistributedLocker;
import com.xxxx.ddd.infrastructure.distributed.redisson.RedisDistributedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TicketDetailCacheService {
    @Autowired
    private RedisDistributedService redisDistributedService;
    @Autowired // Khai bao cache
    private RedisInfrasService redisInfrasService;
    @Autowired
    private TicketDetailDomainService ticketDetailDomainService;

    private String genEventItemKey(Long itemId) {
        return "PRO_TICKET:ITEM:" + itemId;
    }

    public TicketDetail getTicketDefaultCacheNormal(Long id, Long version){
        // 1. get ticket item by redis
        TicketDetail ticketDetail = redisInfrasService.getObject(genEventItemKey(id), TicketDetail.class);
        // 2. YES -> Hit cache
        if(ticketDetail != null){
            log.info("FROM CACHE {}, {}, {}", id, version, ticketDetail);
            return ticketDetail;
        }
        // 3. If NO --> Missing cache

        // 4. Get data from DBS
        ticketDetail = ticketDetailDomainService.getTicketDetailById(id);
        log.info("FROM DBS {}, {}, {}", id, version, ticketDetail);

        // 5. check ticketitem
        if (ticketDetail != null) { // Code nay co van de -> Gia su ticketItem lay ra tu dbs null thi sao, query mãi
            //6. set cache
            redisInfrasService.setObject(genEventItemKey(id), ticketDetail);
        }
        return ticketDetail;
    }

    //REVIEW CODE - SẼ BẮT VIẾT LẠI
    public TicketDetail getTicketDefaulCacheVip (Long id, Long version){
        log.info("Implement getTicketDefaultCacheVip->, {}, {} ", id, version);
        TicketDetail ticketDetail = ticketDetailDomainService.getTicketDetailById(id);//redisInfrasService.getObject(genEventItemKey(id), TicketDetail.class);
        // 2. YES
        if (ticketDetail != null) {
//            log.info("FROM CACHE EXIST {}",ticketDetail);
            return ticketDetail;
        }
//        log.info("CACHE NO EXIST, START GET DB AND SET CACHE->, {}, {} ", id, version);
        // Tao lock process voi KEY
        RedisDistributedLocker locker = redisDistributedService.getDistributedLock("PRO_LOCK_KEY_ITEM"+id);
        try {
            // 1 - Tao lock
            boolean isLock = locker.tryLock(1, 5, TimeUnit.SECONDS);
            // Lưu ý: Cho dù thành công hay không cũng phải unLock, bằng mọi giá.
            if (!isLock) {
//                log.info("LOCK WAIT ITEM PLEASE....{}", version);
                return ticketDetail;
            }
            // Get cache
            ticketDetail = redisInfrasService.getObject(genEventItemKey(id), TicketDetail.class);
            // 2. YES
            if (ticketDetail != null) {
                log.info("FROM CACHE NGON A {}, {}, {}", id, version, ticketDetail);
                return  ticketDetail;
            }
            // 3 -> van khong co thi truy van DB
            ticketDetail = ticketDetailDomainService.getTicketDetailById(id);
            log.info("FROM DBS ->>>> {}, {}", ticketDetail, version);
            if (ticketDetail == null) { // Neu trong dbs van khong co thi return ve not exists;
                log.info("TICKET NOT EXITS....{}", version);
                // set
                redisInfrasService.setObject(genEventItemKey(id), ticketDetail);
                return  ticketDetail;
            }
            // neu co thi set redis
            redisInfrasService.setObject(genEventItemKey(id), ticketDetail); //TTL
            return  ticketDetail;

        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            // Lưu ý: Cho dù thành công hay không cũng phải unLock, bằng mọi giá.
            locker.unlock();
        }
    }
}
