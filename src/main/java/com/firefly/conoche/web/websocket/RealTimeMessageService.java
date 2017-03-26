package com.firefly.conoche.web.websocket;


import com.firefly.conoche.domain.Message;
import com.firefly.conoche.service.dto.MessageDTO;
import com.firefly.conoche.service.MessageService;
import io.github.jhipster.config.JHipsterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import java.nio.channels.AsynchronousChannel;
import java.security.Principal;
import java.time.ZonedDateTime;

import static java.util.concurrent.TimeUnit.SECONDS;

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

    @SubscribeMapping("/topic/RealTimeEventMessageDiscard/")
    public void removeViewedMessage(MessageDTO messageDTO){
        MessageDTO message = this.realTimeEventMessageService.findOne(messageDTO.getId());
        if(message!=null) {
            this.realTimeEventMessageService.delete(messageDTO.getId());
        }
    }

    //cambiar el zoneid
    @SubscribeMapping("/topic/saveRealTimeEventMessage/{idEvent}")
    @SendTo("/topic/RealTimeEventMessage/{idEvent}")
    public MessageDTO sendRealTimeEventMessage(@Payload MessageDTO messageDTO, @DestinationVariable Long idEvent){
        messageDTO.setCreationTime(ZonedDateTime.now());
        messageDTO.setEventId(idEvent);
        return this.realTimeEventMessageService.save(messageDTO);
    }




}
