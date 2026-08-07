package br.com.paulo.chatbot.application.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitingService {

    private final LettuceBasedProxyManager proxyManager;

    public RateLimitingService(@Value("${spring.redis.host:localhost}") String redisHost,
                               @Value("${spring.redis.port:6379}") int redisPort) {
        
        RedisClient redisClient = RedisClient.create("redis://" + redisHost + ":" + redisPort);
        this.proxyManager = LettuceBasedProxyManager.builderFor(redisClient)
                .withExpirationStrategy(io.github.bucket4j.distributed.ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofSeconds(10)))
                .build();
    }

    public Bucket resolveBucket(String tenantId) {
        BucketConfiguration configuration = BucketConfiguration.builder()
                .addLimit(Bandwidth.classic(20, Refill.intervally(20, Duration.ofMinutes(1))))
                .build();
                
        return proxyManager.builder().build(tenantId.getBytes(), configuration);
    }
}
