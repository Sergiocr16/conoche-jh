package com.firefly.conoche.repository.notifications.especificImpl;

import com.firefly.conoche.domain.Event;
import com.firefly.conoche.domain.Local;
import com.firefly.conoche.domain.User;
import com.firefly.conoche.repository.LocalRepository;
import com.firefly.conoche.repository.notifications.NotificationRepositoryImpl;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by melvin on 4/26/2017.
 */
public class LocalRepositoryImpl extends NotificationRepositoryImpl<Local, Long> implements EventRepositoryCustom {
    @Inject
    private LocalRepository localRepository;
    @Override
    public Set<User> notificationRecipients(Local local) {
        return Optional.ofNullable(local)
            .map(Local::getId)
            .map(localRepository::findOne)
            .map(Local::getSubcribers)
            .orElseGet(HashSet::new);
    }
}
