package com.firefly.conoche.web.websocket;


import com.firefly.conoche.service.PromotionCodeService;
import com.firefly.conoche.service.dto.MessageDTO;
import com.firefly.conoche.service.dto.PromotionCodeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.time.ZonedDateTime;

/**
 * Created by sergio on 3/14/2017.
 */
@Controller
public class PromotionCodeWSService {

    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

    private final SimpMessageSendingOperations messagingTemplate;
    private PromotionCodeService promotionCodeService;

    public PromotionCodeWSService(SimpMessageSendingOperations messagingTemplate,
                                  PromotionCodeService promotionCodeService) {
        this.messagingTemplate = messagingTemplate;
        this.promotionCodeService = promotionCodeService;
    }

    @SubscribeMapping("/topic/deletedPromotionCode/{idUser}")
    @SendTo("/topic/deletePromotionCode/{idUser}")
    public PromotionCodeDTO deletePromotionCode(@Payload PromotionCodeDTO promo)throws IOException {
        promo.setActive(false);
        this.promotionCodeService.save(promo);
        return promo;
    }
}
