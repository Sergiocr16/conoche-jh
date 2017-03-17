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
    private int totalMessagues;
    private int currentMessague;
    private List<MessageDTO> messaguesList;

    public RealTimeMessageService(SimpMessageSendingOperations messagingTemplate,
                                  MessageService realTimeEventMessageService) {
        this.messagingTemplate = messagingTemplate;
        this.realTimeEventMessageService = realTimeEventMessageService;
        this.totalMessagues = 0;
        this.currentMessague = 0;
        this.messaguesList = new ArrayList<MessageDTO>();
    }

    //cambiar el zoneid
    @SubscribeMapping("/topic/saveRealTimeEventMessage/{idEvent}")
    @SendTo("/topic/RealTimeEventMessage/{idEvent}")
    public MessageDTO sendRealTimeEventMessage(@Payload MessageDTO messageDTO, @DestinationVariable Long idEvent){
        messageDTO.setCreationTime(ZonedDateTime.now());
        messageDTO.setEventId(idEvent);
        return this.realTimeEventMessageService.save(messageDTO);
    }

    @SubscribeMapping("/topic/RealTimeEventMessageDiscard/")
    public void removeViewedMessage(MessageDTO messageDTO){
        MessageDTO message = this.realTimeEventMessageService.findOne(messageDTO.getId());
        if(message!=null) {
            this.realTimeEventMessageService.delete(messageDTO.getId());
        }
    }

//    @Async
//    @SendTo("/topic/RealTimeEventMessage/{idEvent}")
//    public MessageDTO sendInstantMessage(@Payload MessageDTO messageDTO) throws InterruptedException{
//        this.isBeingView = true;
//        Thread.sleep(3000);
//        this.isBeingView = false;
////        messagingTemplate.convertAndSend("/topic/RealTimeEventMessage/{idEvent}", messageDTO);
//        return messageDTO;
//    }
//
//   public boolean isBeingView() throws InterruptedException {
//        Thread.sleep(1000);
//        return true;
//   }
//
//
//    private void loadMessage(MessageDTO messague){
//     this.messaguesList.add(messague);
//    }
//
    private void removeMessage(MessageDTO messague){
        this.messaguesList.remove(messague);
    }



}
