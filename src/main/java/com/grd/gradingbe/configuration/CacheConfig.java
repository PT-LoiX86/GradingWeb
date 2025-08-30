package com.grd.gradingbe.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Map;

/**
 * Redis Cache Configuration
 * Định nghĩa các cache regions với TTL khác nhau cho từng loại dữ liệu
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // Default cache configuration
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues();

        // Specific cache configurations cho từng loại data
        Map<String, RedisCacheConfiguration> cacheConfigurations = Map.of(
                // Master data - cache lâu dài (24-48h)
                "provinces", defaultConfig.entryTtl(Duration.ofHours(48)),
                "schools", defaultConfig.entryTtl(Duration.ofHours(24)),
                
                // Reference data - cache trung bình (6-12h)
                "majors", defaultConfig.entryTtl(Duration.ofHours(12)),
                "universities", defaultConfig.entryTtl(Duration.ofHours(12)),
                
                // User data - cache ngắn hạn (15-30 phút)
                "student-profiles", defaultConfig.entryTtl(Duration.ofMinutes(30)),
                "user-profiles", defaultConfig.entryTtl(Duration.ofMinutes(15)),
                
                // Search results - cache rất ngắn (5 phút)
                "search-results", defaultConfig.entryTtl(Duration.ofMinutes(5))
        );

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
}
