package com.firefly.conoche.web.websocket;


import com.firefly.conoche.service.dto.MessageDTO;
import com.firefly.conoche.service.MessageService;
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

/**
 * Created by sergio on 3/14/2017.
 */
@Controller
public class RealTimeMessageService {

    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

    private final SimpMessageSendingOperations messagingTemplate;
    private MessageService realTimeEventMessageService;

    public RealTimeMessageService(SimpMessageSendingOperations messagingTemplate,
                                  MessageService realTimeEventMessageService) {
        this.messagingTemplate = messagingTemplate;
        this.realTimeEventMessageService = realTimeEventMessageService;
    }

    //cambiar el zoneid
    @SubscribeMapping("/topic/saveRealTimeEventMessage/{idEvent}")
    @SendTo("/topic/RealTimeEventMessage/{idEvent}")
    public MessageDTO sendRealTimeEventMessage(@Payload MessageDTO messageDTO,
                                                        @DestinationVariable Long idEvent,
                                                        StompHeaderAccessor stompHeaderAccessor,
                                                        Principal principal) {
        messageDTO.setCreationTime(ZonedDateTime.now());
        messageDTO.setEventId(idEvent);
        messagingTemplate.convertAndSend("/topic/RealTimeEventMessage/{idEvent}", messageDTO);
        return messageDTO;
    }
}
