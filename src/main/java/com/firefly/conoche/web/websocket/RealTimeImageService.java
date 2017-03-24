package com.firefly.conoche.web.websocket;

import com.firefly.conoche.service.RealTimeEventImageService;
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

/**
 * Created by melvin on 3/10/2017.
 */
@Controller
public class RealTimeImageService {

    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

    private final SimpMessageSendingOperations messagingTemplate;
    private RealTimeEventImageService realTimeEventImageService;
    private CRealTimeEventImageService crealTimeEventImageService;

    public RealTimeImageService(SimpMessageSendingOperations messagingTemplate,
                                RealTimeEventImageService realTimeEventImageService,
                                CRealTimeEventImageService crealTimeEventImageService) {
        this.messagingTemplate = messagingTemplate;
        this.realTimeEventImageService = realTimeEventImageService;
        this.crealTimeEventImageService = crealTimeEventImageService;
    }

    //Mejorar despues.
    @MessageExceptionHandler(IOException.class)
    @SendToUser("/queue/errors")
    public IOException handleExceptions(IOException e) {
        log.error("Error handling message: " + e.getMessage());
        return e;
    }


    //cambiar el zoneid
    @SubscribeMapping("/topic/saveRealTimeEventImage/{idEvent}")
    @SendTo("/topic/RealTimeEventImage/{idEvent}")
    public RealTimeEventImageDTO sendRealTimeEventImage(@Payload RealTimeEventImageDTO RTEimageDTO,
                                              @DestinationVariable Long idEvent,
                                              StompHeaderAccessor stompHeaderAccessor,
                                              Principal principal) {
        RTEimageDTO.setCreationTime(ZonedDateTime.now());
        RTEimageDTO.setEventId(idEvent);
        return realTimeEventImageService.save(RTEimageDTO);
    }

    @SubscribeMapping("/topic/deleteRealTimeEventImage/{idEvent}")
    @SendTo("/topic/deletedRealTimeEventImage/{idEvent}")
    public RealTimeEventImageDTO deleteRealTimeEventImage(@Payload RealTimeEventImageDTO RTEimageDTO,
                                                        @DestinationVariable Long idEvent,
                                                        StompHeaderAccessor stompHeaderAccessor,
                                                        Principal principal) throws IOException {

        crealTimeEventImageService.delete(RTEimageDTO);
        return RTEimageDTO;
    }
}
