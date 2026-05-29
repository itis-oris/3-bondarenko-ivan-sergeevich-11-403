package com.itis.cryptotracker.config;

import freemarker.template.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.time.Instant;
import java.util.Date;

@Configuration
@RequiredArgsConstructor
public class FreeMarkerConfig {

    private final FreeMarkerConfigurer freeMarkerConfigurer;

    @PostConstruct
    public void configureInstantSupport() {
        freeMarkerConfigurer.getConfiguration().setObjectWrapper(
                new DefaultObjectWrapper(freemarker.template.Configuration.VERSION_2_3_33) {
                    @Override
                    public TemplateModel wrap(Object obj) throws TemplateModelException {
                        if (obj instanceof Instant inst) {
                            return new SimpleDate(Date.from(inst), TemplateDateModel.DATETIME);
                        }
                        return super.wrap(obj);
                    }
                }
        );
    }
}
