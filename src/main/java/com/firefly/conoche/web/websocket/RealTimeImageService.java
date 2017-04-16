package com.firefly.conoche.web.websocket;

import com.firefly.conoche.security.SecurityUtils;
import com.firefly.conoche.service.RealTimeEventImageService;
import com.firefly.conoche.service.customService.CAuthOwnerService;
import com.firefly.conoche.service.customService.CRealTimeEventImageService;
import com.firefly.conoche.service.dto.RealTimeEventImageDTO;
import com.firefly.conoche.web.websocket.dto.ActivityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;


import java.io.IOException;
import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Created by melvin on 3/10/2017.
 * Utilizar rest + SimpMessageSendingOperations vs Stomp
 */
@Controller
public class RealTimeImageService {

    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

    private final SimpMessageSendingOperations messagingTemplate;
    private final RealTimeEventImageService realTimeEventImageService;
    private final CRealTimeEventImageService crealTimeEventImageService;
    private final CAuthOwnerService cAuthOwnerService;

    public RealTimeImageService(SimpMessageSendingOperations messagingTemplate,
                                RealTimeEventImageService realTimeEventImageService,
                                CRealTimeEventImageService crealTimeEventImageService,
                                CAuthOwnerService cAuthOwnerService ) {

        this.messagingTemplate          = messagingTemplate;
        this.realTimeEventImageService  = realTimeEventImageService;
        this.crealTimeEventImageService = crealTimeEventImageService;
        this.cAuthOwnerService          = cAuthOwnerService;
    }

    //Mejorar despues.
    @MessageExceptionHandler({IOException.class, RuntimeException.class})
    @SendToUser("/queue/errors")
    public String handleExceptions(Throwable e) {
        String message = e.getMessage();
        log.error("Error handling message: " + message);
        return message;
    }


    @SubscribeMapping("/topic/saveRealTimeEventImage/{idEvent}")
    public void sendRealTimeEventImage(@Payload RealTimeEventImageDTO RTEimageDTO,
                                              @DestinationVariable Long idEvent) {
        RTEimageDTO.setCreationTime(ZonedDateTime.now().withNano(0));
        RTEimageDTO.setEventId(idEvent);
        messagingTemplate.convertAndSend(
            "/topic/RealTimeEventImage/" + idEvent,
            realTimeEventImageService.save(RTEimageDTO));
    }

    @SubscribeMapping("/topic/deleteRealTimeEventImage/{idRealTimeImage}")
    public void deleteRealTimeEventImage(@DestinationVariable Long idRealTimeImage) throws IOException {
        RealTimeEventImageDTO imageDTO = crealTimeEventImageService.delete(idRealTimeImage);
        messagingTemplate.convertAndSend("/topic/deletedRealTimeEventImage/" + imageDTO.getEventId(), imageDTO);
    }
}
