package com.firefly.conoche.config;

import com.cloudinary.*;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by melvin on 3/22/2017.
 */
@Configuration
public class CloudinaryConfiguration {
    private ApplicationProperties applicationProperties;

    public CloudinaryConfiguration(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Bean
    public  Cloudinary cloudinaryConfig() {
        ApplicationProperties.Cloudinary cloudinary = applicationProperties.getCloudinary();
        Map config = new HashMap();
        config.put("cloud_name", cloudinary.getCloudName());
        config.put("api_key", cloudinary.getApiKey());
        config.put("api_secret", cloudinary.getApiSecret());
        return new Cloudinary(config);
    }
}

