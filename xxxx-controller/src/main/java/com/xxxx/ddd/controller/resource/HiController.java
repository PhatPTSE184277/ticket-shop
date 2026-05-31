package com.xxxx.ddd.controller.resource;

import com.xxxx.ddd.application.service.event.EventAppService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;

@RestController
@RequestMapping("/hello")
public class HiController {
    @Autowired
    private EventAppService eventAppService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/hi")
    @RateLimiter(name = "backendA", fallbackMethod = "fallbackHello")
    public String hello(){
        return eventAppService.sayHi("Hi");
    }

    public String fallbackHello(Throwable throwable){
        return "Too many request!";
    }

    @GetMapping("/hi/v1")
    @RateLimiter(name = "backendB", fallbackMethod = "fallbackHello")
    public String sayHi(){
        return eventAppService.sayHi("Ho");
    }

    private static final SecureRandom random = new SecureRandom();
    @GetMapping("/circuit/breakers")
    @CircuitBreaker(name = "checkerRandom", fallbackMethod = "fallbackCircuitBreaker")
    public String circuitBreakers(){
        int productId = random.nextInt(20) + 1;
        String url = "https://fakestoreapi.com/products/" + productId
                ;
        return restTemplate.getForObject(url, String.class);
    }

    public static  String fallbackCircuitBreaker(Throwable throwable){
        return "Service fakestoreapi error!";
    }
}
