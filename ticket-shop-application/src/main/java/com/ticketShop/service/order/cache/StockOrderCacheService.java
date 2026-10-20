package com.ticketShop.service.order.cache;

import com.ticketShop.cache.RedisInfraService;
import com.ticketShop.model.cache.TicketCache;
import com.ticketShop.service.ticket.cache.TicketCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;

@Service
@Slf4j
public class StockOrderCacheService {

    private static final String LUA_DEDUCT =
            "local stock = redis.call('GET', KEYS[1]); " +
            "if stock == false then return -1 end; " +
            "stock = tonumber(stock); " +
            "if (stock >= tonumber(ARGV[1])) then " +
            "   redis.call('SET', KEYS[1], stock - tonumber(ARGV[1])); " +
            "   return 1; " +
            "end; " +
            "return 0; ";

    private static final String LUA_RESTORE =
            "local stock = redis.call('GET', KEYS[1]); " +
            "if (stock) then " +
            "   redis.call('SET', KEYS[1], tonumber(stock) + tonumber(ARGV[1])); " +
            "   return 1; " +
            "end; " +
            "return 0;";

    private static final DefaultRedisScript<Long> SCRIPT_DEDUCT = new DefaultRedisScript<>(LUA_DEDUCT, Long.class);

    private static final DefaultRedisScript<Long> SCRIPT_RESTORE = new DefaultRedisScript<>(LUA_RESTORE, Long.class);

    private String getKeyStockItemCache(Long ticketId) {
        return "TICKET:"+ ticketId + ":STOCK";
    }

    @Autowired
    private TicketCacheService ticketCacheService;

    @Autowired
    private RedisInfraService redisInfraService;

    public boolean addStockAvailableToCache(Long ticketId){
        // That's remember check validation(*)
        if (ticketId == null) return false;

        // get stock_available from mysql
        TicketCache ticketCache = ticketCacheService.getTicket(ticketId, null);
        if (ticketCache == null || ticketCache.getTicket() == null){
            return false;
        }
        String keyStockTicketCache = getKeyStockItemCache(ticketId);
        log.info("get->getKeyStockTicketCache() | {}, {}, {}", ticketId, keyStockTicketCache,
                ticketCache.getTicket().getStockAvailable());

        redisInfraService.setInt(keyStockTicketCache, ticketCache.getTicket().getStockAvailable());
        return true;
    }

    // decreaseStockCache
    public int decreaseStockCache(Long ticketId, Integer quantity){
        // 1. Get Stock Available
        String keyStockNormal = getKeyStockItemCache(ticketId);
        int stockAvailable = redisInfraService.getInt(keyStockNormal);
        log.info("stockAvailable Normal: {}, {}, {} ", keyStockNormal, stockAvailable, String.valueOf(stockAvailable - quantity));
        // 2. Decrease Stock

        if (stockAvailable >= quantity){
            redisInfraService.setInt(keyStockNormal, stockAvailable - quantity);
            log.info("stockAvailable racing...: {}", stockAvailable - quantity);
            return 1;
        }
        return 0;
    }

    public int decreaseStockCacheByLUA(Long ticketId, Integer quantity) {
        String key = getKeyStockItemCache(ticketId);
        Long result = redisInfraService.getRedisTemplate().execute(SCRIPT_DEDUCT, Collections.singletonList(key), quantity);
        return result != null ? result.intValue() : -1;
    }

    // Trả về giá hiệu lực: priceFlash nếu có, ngược lại priceOriginal. -1 nếu không tìm thấy ticket.
    public long getEffectivePrice(Long ticketId) {
        TicketCache ticketCache = ticketCacheService.getTicket(ticketId, null);
        if (ticketCache == null || ticketCache.getTicket() == null) return -1L;
        BigDecimal flash = ticketCache.getTicket().getPriceFlash();
        BigDecimal original = ticketCache.getTicket().getPriceOriginal();
        if (flash != null && flash.compareTo(BigDecimal.ZERO) > 0) return flash.longValue();
        return original != null ? original.longValue() : -1L;
    }

    // tăng stock trong cache nếu user cancel đơn hàng trong khi diễn ra flash sale
    public boolean increaseStockCache(Long ticketId, Integer quantity) {
        String key = getKeyStockItemCache(ticketId);
        Long result = redisInfraService.getRedisTemplate().execute(SCRIPT_RESTORE, Collections.singletonList(key), quantity);
        return result != null && result == 1;
    }
}

