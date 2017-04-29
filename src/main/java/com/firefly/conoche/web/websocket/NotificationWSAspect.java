package com.firefly.conoche.web.websocket;

import com.firefly.conoche.domain.ActionObject;
import com.firefly.conoche.domain.enumeration.ActionObjectType;
import com.firefly.conoche.service.dto.*;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Stream;

/**
 * Created by melvin on 4/22/2017.
 * Aspecto encargado de enviar notificaciones por medio de websockets.
 */

@Order(1)
@Aspect
@Controller
public class NotificationWSAspect {
    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);
    private static final String USER_SUBSCRIPTION_URL = "/queue/notifications";
    private static final String DELETE_SUBSCRIPTION_URL = "/queue/notifications/dead";
    @Inject
    private SimpMessageSendingOperations messagingTemplate;

    /**
     * Advice que intercepta el metodo encargado de crear las notificaciones.
     * @param retVal es un Future con las lista de notificaciones creadas.
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @AfterReturning(
        pointcut="execution(public * com.firefly.conoche.service.customService.CNotificationService+.createNotifications(..))",
        returning="retVal")
    public void notificationCreateAdvice(CompletableFuture<List<DetailNotificationDTO>> retVal) throws InterruptedException, ExecutionException {
        try {
            retVal.thenAccept(l -> l.forEach(this::sendToUser));
        } catch(Throwable s) {
            log.error(s.getMessage());
        }
    }

    /**
     * Advice que intercepta la desactivacion de las notificaciones.
     * @param retVal Lista login de los usuarios.
     * @throws InterruptedException Exepcion en caso de que el thread se interumpido.
     * @throws ExecutionException
     */
    @AfterReturning(
        pointcut="execution(public * com.firefly.conoche.service.customService.CNotificationService+.deactivateActionObjects(..))",
        returning="retVal")
    public void notificationDeleteAdvice(CompletableFuture<Stream<String>> retVal)
            throws InterruptedException, ExecutionException {
        try {
            WrapperDTO<?> empty = new WrapperDTO<>(null);
            retVal.thenAccept( logins -> logins
                .forEach(login -> messagingTemplate.convertAndSendToUser(
                    login, DELETE_SUBSCRIPTION_URL, empty)));

        } catch(Throwable s) {
            log.error(s.getMessage());
        }
    }

    /**
     *  Envia una notificacion por websockets al usuario al cual le pertenece.
     * @param notificationDTO notificacion.
     */
    private void sendToUser(DetailNotificationDTO notificationDTO) {
        messagingTemplate.convertAndSendToUser(notificationDTO.getUserLogin(),
            USER_SUBSCRIPTION_URL, notificationDTO);
    }
}
