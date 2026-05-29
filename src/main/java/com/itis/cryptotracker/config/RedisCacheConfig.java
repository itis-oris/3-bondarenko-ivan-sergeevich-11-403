package com.itis.cryptotracker.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.itis.cryptotracker.client.CoinGeckoClient;
import com.itis.cryptotracker.client.CoinPriceDto;
import com.itis.cryptotracker.config.CoinDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.List;
import java.util.Map;


@Configuration
public class RedisCacheConfig {

    public static final String CACHE_COIN_PRICES = "coin-prices";
    public static final String CACHE_COIN_DETAILS = "coin-details";

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        JavaType pricesType = mapper.getTypeFactory()
                .constructCollectionType(List.class, CoinPriceDto.class);
        Jackson2JsonRedisSerializer<Object> pricesSerializer =
                new Jackson2JsonRedisSerializer<>(mapper, pricesType);

        Jackson2JsonRedisSerializer<CoinDetails> detailsSerializer =
                new Jackson2JsonRedisSerializer<>(mapper, CoinDetails.class);

        RedisCacheConfiguration baseCfg = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(30))
                .disableCachingNullValues()
                .serializeKeysWith(SerializationPair.fromSerializer(new StringRedisSerializer()));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(baseCfg)
                .withInitialCacheConfigurations(Map.of(
                        CACHE_COIN_PRICES, baseCfg
                                .entryTtl(Duration.ofSeconds(20))
                                .serializeValuesWith(SerializationPair.fromSerializer(pricesSerializer)),
                        CACHE_COIN_DETAILS, baseCfg
                                .entryTtl(Duration.ofMinutes(30))
                                .serializeValuesWith(SerializationPair.fromSerializer(detailsSerializer))
                ))
                .build();
    }
}
