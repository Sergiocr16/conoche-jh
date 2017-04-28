package com.firefly.conoche.service.customService;


import com.firefly.conoche.domain.ActionObject;
import com.firefly.conoche.domain.Notification;
import com.firefly.conoche.domain.ObjectChange;
import com.firefly.conoche.domain.User;
import com.firefly.conoche.domain.enumeration.ActionObjectType;
import com.firefly.conoche.domain.enumeration.ActionType;
import com.firefly.conoche.domain.interfaces.IEntity;
import com.firefly.conoche.repository.ActionObjectRepository;
import com.firefly.conoche.repository.NotificationRepository;
import com.firefly.conoche.repository.ObjectChangeRepository;
import com.firefly.conoche.repository.customRepository.CNotificationRepository;
import com.firefly.conoche.repository.notifications.NotifyRepository;
import com.firefly.conoche.service.ActionObjectService;
import com.firefly.conoche.service.NotificationService;
import com.firefly.conoche.service.ObjectChangeService;
import com.firefly.conoche.service.dto.*;
import com.firefly.conoche.service.mapper.DetailNotificationMapper;
import com.firefly.conoche.service.mapper.NotificationMapper;
import com.firefly.conoche.service.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by melvin on 4/20/2017.
 */
@Service
@Transactional
public class CNotificationService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final NotificationRepository notificationRepository;
    private final CNotificationRepository cNotificationRepository;
    private final ActionObjectRepository actionObjectRepository;
    private final ObjectChangeRepository objectChangeRepository;
    private final DetailNotificationMapper detailNotificationMapper;
    private final NotificationMailService notificationMailService;


    public CNotificationService(NotificationRepository notificationRepository,
                                CNotificationRepository cNotificationRepository,
                                ActionObjectRepository actionObjectRepository,
                                ObjectChangeRepository objectChangeRepository,
                                DetailNotificationMapper detailNotificationMapper,
                                NotificationMailService notificationMailService) {

        this.notificationRepository   = notificationRepository;
        this.cNotificationRepository  = cNotificationRepository;
        this.actionObjectRepository   = actionObjectRepository;
        this.objectChangeRepository   = objectChangeRepository;
        this.detailNotificationMapper = detailNotificationMapper;
        this.notificationMailService =   notificationMailService;
    }


    @Async
    public Future<List<DetailNotificationDTO>> createNotifications(Stream<String> changesStream,
                                                            Supplier<Set<User>> recipientsSupplier,
                                                            Long id,
                                                            ActionObjectType type,
                                                             ActionType actionType) {
        Set<User> recipients = recipientsSupplier.get();
        log.error(Integer.toString(recipients.size()));
        if(recipients.isEmpty()) {
            return emptyResult();
        }
        Set<ObjectChange> oChanges = changesStream
            .map(this::createChange)
            .collect(Collectors.toSet());

        if(ActionType.UPDATE == actionType
            && oChanges.isEmpty()) {

            return emptyResult();
        }

        ActionObject ao =  createActionObject(id, type,
            oChanges, actionType);

        List<DetailNotificationDTO> notifications = recipients
            .stream()
            .map( u ->createNotification(u, ao))
            .peek( n-> n.getActionObject().getChanges())
            .map(detailNotificationMapper::notificationToNotificationDTO)
            .collect(Collectors.toList());

        notificationMailService.sendMails(recipients, ao);

        return new AsyncResult<>(notifications);
    }

    private Future<List<DetailNotificationDTO>> emptyResult() {
        return new AsyncResult<>(new ArrayList<>());
    }

    private ActionObject createActionObject(Long id,
                                            ActionObjectType objectType,
                                            Set<ObjectChange> oChanges,
                                            ActionType actionType) {

        ActionObject ao = new ActionObject();
        ao.setObjectType(objectType);
        ao.setCreationTime(ZonedDateTime.now());
        ao.setActionType(actionType);
        ao.setObjectId(id);
        ao.setActive(true);
        oChanges.forEach(ao::addChanges);
        return actionObjectRepository.save(ao);
    }

    private Notification createNotification(User u, ActionObject a) {
        Notification n = new Notification();
        n.setUser(u);
        n.setActionObject(a);
        n.setIsRead(false);
        return notificationRepository.save(n);
    }

    private ObjectChange createChange(String fieldName) {
        ObjectChange oc = new ObjectChange();
        oc.setFieldName(fieldName);
        return objectChangeRepository.save(oc);
    }

    @Transactional(readOnly = true)
    public Page<DetailNotificationDTO> getAllNotificationsFromCurrent(Pageable page, Boolean isRead) {
        return cNotificationRepository.findByUserIsCurrentUser(page, isRead)
            .map(detailNotificationMapper::notificationToNotificationDTO);
    }


    @Async
    public Future<Stream<String>> deactivateActionObjects(ActionObjectType type, Long objectId) {
        List<ActionObject> actions = actionObjectRepository
            .findByObjectTypeAndObjectIdAndActiveTrue(type, objectId);

        Stream<String> users = cNotificationRepository
            .findUsersWithPendingEnitityNotifications(objectId, type)
            .stream().map(User::getLogin);

        actions.stream().peek(a -> log.error(actions.toString()))
            .peek(a ->  a.setActive(false))
            .forEach(actionObjectRepository::save);

        return new AsyncResult<>(users);
    }

}
