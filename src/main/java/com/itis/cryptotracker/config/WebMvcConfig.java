package com.itis.cryptotracker.config;

import com.itis.cryptotracker.converter.StringToCoinConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigDecimal;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final StringToCoinConverter stringToCoinConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToCoinConverter);
        registry.addConverter(String.class, BigDecimal.class, source -> {
            if (source == null || source.isBlank()) return null;
            return new BigDecimal(source.trim().replace(',', '.'));
        });
    }
}
