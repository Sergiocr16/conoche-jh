package com.firefly.conoche.config;

import com.firefly.conoche.aop.logging.LoggingAspect;

import com.firefly.conoche.service.aspects.NotificationAspect;
import com.firefly.conoche.web.websocket.NotificationWSAspect;
import io.github.jhipster.config.JHipsterConstants;

import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

@Configuration
@EnableAspectJAutoProxy
public class AspectsConfiguration {

    @Bean
    @Profile(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)
    public LoggingAspect loggingAspect(Environment env) {
        return new LoggingAspect(env);
    }

    @Bean
    public NotificationAspect notificationAspect() {
        return new NotificationAspect();
    }

    @Bean
    public NotificationWSAspect notificationWSAspect() { return new NotificationWSAspect(); }
}
