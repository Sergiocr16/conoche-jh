package com.firefly.conoche.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Properties specific to JHipster.
 *
 * <p>
 *     Properties are configured in the application.yml file.
 * </p>
 */
@Component
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Cloudinary cloudinary = new Cloudinary();
    private final NotificationsUrl notificationsUrl = new NotificationsUrl();

    public Cloudinary getCloudinary() {
        return cloudinary;
    }

    public NotificationsUrl getNotificationsUrl() {
        return notificationsUrl;
    }

    public static class Cloudinary {
        private String cloudName;
        private String apiKey;
        private String apiSecret;

        public String getCloudName() {
            return cloudName;
        }

        public void setCloudName(String cloudName) {
            this.cloudName = cloudName;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getApiSecret() {
            return apiSecret;
        }

        public void setApiSecret(String apiSecret) {
            this.apiSecret = apiSecret;
        }
    }
    public static class NotificationsUrl{

        private String user;
        private String local;
        private String event;
        private String promotion;
        private String message;
        private String rating;
        private String eventImage;
        private String realtimeEventImage;
        private String localImage;


        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getLocal() {
            return local;
        }

        public void setLocal(String local) {
            this.local = local;
        }

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public String getPromotion() {
            return promotion;
        }

        public void setPromotion(String promotion) {
            this.promotion = promotion;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getEventImage() {
            return eventImage;
        }

        public void setEventImage(String eventImage) {
            this.eventImage = eventImage;
        }

        public String getRealtimeEventImage() {
            return realtimeEventImage;
        }

        public void setRealtimeEventImage(String realtimeEventImage) {
            this.realtimeEventImage = realtimeEventImage;
        }

        public String getLocalImage() {
            return localImage;
        }

        public void setLocalImage(String localImage) {
            this.localImage = localImage;
        }
    }


}
