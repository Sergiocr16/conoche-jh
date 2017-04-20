package com.firefly.conoche.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.firefly.conoche.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.SocialUserConnection.class.getName(), jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.Message.class.getName(), jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.Action.class.getName(), jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.Action.class.getName() + ".objects", jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.Action.class.getName() + ".users", jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.Event.class.getName(), jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.Event.class.getName() + ".promotions", jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.Event.class.getName() + ".images", jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.Event.class.getName() + ".realTimeImages", jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.Event.class.getName() + ".attendingUsers", jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.Event.class.getName() + ".services", jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.Event.class.getName() + ".messages", jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.EventImage.class.getName(), jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.ImagenLocal.class.getName(), jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.Local.class.getName(), jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.Local.class.getName() + ".images", jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.Local.class.getName() + ".events", jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.Local.class.getName() + ".schedules", jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.Local.class.getName() + ".services", jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.Local.class.getName() + ".subcribers", jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.Local.class.getName() + ".ratings", jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.ActionObject.class.getName(), jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.Promotion.class.getName(), jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.Promotion.class.getName() + ".codes", jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.PromotionCode.class.getName(), jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.RatingLocal.class.getName(), jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.RealTimeEventImage.class.getName(), jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.Schedule.class.getName(), jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.Servicio.class.getName(), jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.Category.class.getName(), jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.Notification.class.getName(), jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.ObjectChange.class.getName(), jcacheConfiguration);
            cm.createCache(com.firefly.conoche.domain.ActionObject.class.getName() + ".changes", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
