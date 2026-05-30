package com.ytk.ytkoj.global.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Map;

@Configuration
@EnableCaching
public class CachingConfig {

    // ConcurrentCacheManager는 TTL 미지원이므로 CaffeineCacheManager 사용
    // Caffeine 캐시 매니저도 thread-safe이다. 해당 캐시 매니저 내부에서도 concurrentHashMap 사용
    @Bean
    public CacheManager cacheManager(){
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofMinutes(5))
                        .maximumSize(1000)
        );
        return cacheManager;
    }

    // 캐시를 관리하는 캐시 매니저를 설정합니다.
    // RedisCacheManager는 CacheManager를 상속함
    // TTL 사용 위해 CacheManager가 아닌 Redis 사용
//    @Bean
//    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory){
//        // 레디스 config는 ttl, prefix, RedisSerializer 구현체들 설정을 지원한다.
//        // 기본 캐시 설정
//        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(5)) // ttl 설정
//                .disableCachingNullValues(); // null 값은 캐싱하지 않음
//
//        Map<String, RedisCacheConfiguration> profiles = Map.of(
//                "SLCache", RedisCacheConfiguration.defaultCacheConfig(), // 기본 설정 그대로 5분으로 설정
//                "LLCache", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(2)) // 2시간으로 설정
//        );
//
//        return RedisCacheManager
//                .builder(connectionFactory)
//                .cacheDefaults(redisCacheConfiguration)
//                .withInitialCacheConfigurations(profiles) // 여러 캐시 프로필 설정
//                .transactionAware() // DB 트랜잭션 작업과 동기화 즉, DB 커밋 이후에 캐싱 실행
//                .build();
//    }

}
