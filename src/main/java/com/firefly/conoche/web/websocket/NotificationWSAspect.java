package com.firefly.conoche.web.websocket;

import com.firefly.conoche.service.dto.NotificationDTO;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by melvin on 4/22/2017.
 */

@Aspect
@Controller
public class NotificationWSAspect {
    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);
    private static final String SUBSCRIPTION_URL = "/queue/notifications";
    @Inject
    private SimpMessageSendingOperations messagingTemplate;

    @AfterReturning(
        pointcut="execution(public * com.firefly.conoche.service.customService.CNotificationService+.createNotifications(..))",
        returning="retVal")
    public void notificationAdvice(Future<List<NotificationDTO>> retVal) throws InterruptedException, ExecutionException{
        log.error(Thread.currentThread().getName());
        List<NotificationDTO> notifications = retVal.get();
        notifications.forEach(this::sendToUser);
        log.error(Thread.currentThread().getName());
    }


    private void sendToUser(NotificationDTO notificationDTO) {
        messagingTemplate.convertAndSendToUser(notificationDTO.getUserLogin(),
            SUBSCRIPTION_URL, notificationDTO);
    }
}
