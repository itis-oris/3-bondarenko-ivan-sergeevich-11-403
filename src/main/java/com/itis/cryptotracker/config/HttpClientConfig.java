package com.itis.cryptotracker.config;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class HttpClientConfig {

    @Value("${app.coingecko.request-timeout-ms}")
    private long requestTimeoutMs;

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(Duration.ofMillis(requestTimeoutMs))
                .readTimeout(Duration.ofMillis(requestTimeoutMs))
                .writeTimeout(Duration.ofMillis(requestTimeoutMs))
                .retryOnConnectionFailure(true)
                .build();
    }
}
