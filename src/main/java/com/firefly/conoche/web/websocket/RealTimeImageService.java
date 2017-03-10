package com.firefly.conoche.web.websocket;

import com.firefly.conoche.service.RealTimeEventImageService;
import com.firefly.conoche.service.dto.RealTimeEventImageDTO;
import com.firefly.conoche.web.websocket.dto.ActivityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;


import java.security.Principal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by melvin on 3/10/2017.
 */
@Controller
public class RealTimeImageService {

    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

    private final SimpMessageSendingOperations messagingTemplate;
    private RealTimeEventImageService realTimeEventImageService;

    public RealTimeImageService(SimpMessageSendingOperations messagingTemplate,
                                RealTimeEventImageService realTimeEventImageService) {
        this.messagingTemplate = messagingTemplate;
        this.realTimeEventImageService = realTimeEventImageService;
    }

    //cambiar el zoneid
    @SubscribeMapping("/topic/saveRTimage/{idEvent}")
    @SendTo("/topic/RTimage/{idEvent}")
    public RealTimeEventImageDTO sendActivity(@Payload RealTimeEventImageDTO RTEimageDTO,
                                              @DestinationVariable Long idEvent,
                                              StompHeaderAccessor stompHeaderAccessor,
                                              Principal principal) {
        RTEimageDTO.setImage(null);
        RTEimageDTO.setCreationTime(ZonedDateTime.now());
        RTEimageDTO.setEventId(idEvent);
        return realTimeEventImageService.save(RTEimageDTO);
    }
}
