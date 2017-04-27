package com.firefly.conoche.service.customService;

import com.firefly.conoche.domain.ActionObject;
import com.firefly.conoche.domain.Notification;
import com.firefly.conoche.domain.User;
import com.firefly.conoche.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by melvin on 4/25/2017.
 */
@Service
public class NotificationMailService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public final MailService mailService;

    public NotificationMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Async
    void sendMails(Set<User> recipients, ActionObject ao) {
        recipients.forEach( u -> mailService.sendNotificationEmail(u, ao));
    }
}
