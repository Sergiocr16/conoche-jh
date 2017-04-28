package com.firefly.conoche.repository.notifications.especificImpl;

import com.firefly.conoche.domain.Event;
import com.firefly.conoche.domain.Local;
import com.firefly.conoche.domain.User;
import com.firefly.conoche.repository.EventRepository;
import com.firefly.conoche.repository.LocalRepository;
import com.firefly.conoche.repository.notifications.NotificationRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by melvin on 4/20/2017.
 */


public class EventRepositoryImpl extends NotificationRepositoryImpl<Event, Long> implements EventRepositoryCustom {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    private EventRepository eventRepository;

    @Inject
    private LocalRepository localRepository;

    @Override
    public Set<User> notificationRecipients(Event event) {
        return Optional.ofNullable(event)
            .map(Event::getLocal)
            .map(Local::getId)
            .map(localRepository::findOne)
            .map(Local::getSubcribers)
            .orElseGet(HashSet::new);
    }
}
