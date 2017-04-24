package com.firefly.conoche.web.websocket;

import com.firefly.conoche.domain.ActionObject;
import com.firefly.conoche.domain.enumeration.ActionObjectType;
import com.firefly.conoche.service.dto.NotificationDTO;
import com.firefly.conoche.service.dto.NotificationEntityDTO;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Stream;

/**
 * Created by melvin on 4/22/2017.
 */

@Aspect
@Controller
public class NotificationWSAspect {
    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);
    private static final String USER_SUBSCRIPTION_URL = "/queue/notifications";
    private static final String DELETE_SUBSCRIPTION_URL = "/queue/notifications/dead";
    @Inject
    private SimpMessageSendingOperations messagingTemplate;

    @AfterReturning(
        pointcut="execution(public * com.firefly.conoche.service.customService.CNotificationService+.createNotifications(..))",
        returning="retVal")
    public void notificationCreateAdvice(Future<List<NotificationDTO>> retVal) throws InterruptedException, ExecutionException {
        List<NotificationDTO> notifications = retVal.get();
        notifications.forEach(this::sendToUser);
    }

    @AfterReturning(
        pointcut="execution(public * com.firefly.conoche.service.customService.CNotificationService+.deactivateActionObjects(..)) && args(type,id,..)",
        returning="retVal")
    public void notificationDeleteAdvice(Future<Stream<String>> retVal, ActionObjectType type, Long id)
            throws InterruptedException, ExecutionException {

        NotificationEntityDTO notificationEntityDTO = new NotificationEntityDTO();
        notificationEntityDTO.setId(id);
        notificationEntityDTO.setType(type);
        retVal.get()
            .peek(log::error)
            .forEach(login -> messagingTemplate.convertAndSendToUser(
                login, DELETE_SUBSCRIPTION_URL, notificationEntityDTO));
    }

    private void sendToUser(NotificationDTO notificationDTO) {
        messagingTemplate.convertAndSendToUser(notificationDTO.getUserLogin(),
            USER_SUBSCRIPTION_URL, notificationDTO);
    }
}
