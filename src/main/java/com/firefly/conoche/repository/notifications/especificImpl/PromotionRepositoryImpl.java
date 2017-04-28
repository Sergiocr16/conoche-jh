package com.firefly.conoche.repository.notifications.especificImpl;

import com.firefly.conoche.domain.Event;
import com.firefly.conoche.domain.Local;
import com.firefly.conoche.domain.Promotion;
import com.firefly.conoche.domain.User;
import com.firefly.conoche.repository.EventRepository;
import com.firefly.conoche.repository.PromotionRepository;
import com.firefly.conoche.repository.notifications.NotificationRepositoryImpl;
import com.firefly.conoche.service.LocalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by melvin on 4/27/2017.
 */
public class PromotionRepositoryImpl extends NotificationRepositoryImpl<Promotion, Long> implements PromotionRepositoryCustom {


    private final Logger log = LoggerFactory.getLogger(LocalService.class);
    @Inject
    private PromotionRepository promotionRepository;
    @Inject
    private EventRepository eventRepository;

    @Override
    public Set<User> notificationRecipients(Promotion promotion) {

        return Optional.ofNullable(promotion)
            .map(Promotion::getEvent)
            .map(Event::getId)
            .map(eventRepository::findOne)
            .map(eventRepository::notificationRecipients)
            .orElseGet(HashSet::new);
    }
}
