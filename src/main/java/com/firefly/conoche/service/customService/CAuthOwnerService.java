package com.firefly.conoche.service.customService;

import com.firefly.conoche.domain.Event;
import com.firefly.conoche.domain.Local;
import com.firefly.conoche.repository.EventRepository;
import com.firefly.conoche.service.EventService;
import com.firefly.conoche.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by melvin on 3/29/2017.
 */
@Service
@Transactional
public class CAuthOwnerService {

    private final UserService userService;
    private final EventRepository eventRepository;


    public CAuthOwnerService(UserService userService,EventRepository eventRepository) {
        this.userService = userService;
        this.eventRepository = eventRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Boolean> currentUserIsOwner(Long idEvent) {
        return Optional
            .ofNullable(userService.getUserWithAuthorities())
            .flatMap(user -> Optional
                .ofNullable(eventRepository.findOne(idEvent))
                .map(Event::getLocal)
                .map(Local::getOwner)
                .map(owner -> owner.getId() == user.getId()));
    }
}

