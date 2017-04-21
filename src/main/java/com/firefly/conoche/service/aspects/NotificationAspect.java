package com.firefly.conoche.service.aspects;

import com.firefly.conoche.domain.User;
import com.firefly.conoche.domain.enumeration.ActionType;
import com.firefly.conoche.domain.interfaces.IEntity;
import com.firefly.conoche.repository.notifications.NotifyRepository;

import com.firefly.conoche.service.customService.CNotificationService;
import com.firefly.conoche.service.dto.UserDTO;
import com.firefly.conoche.service.mapper.UserMapper;
import org.aspectj.apache.bcel.Repository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Created by melvin on 4/19/2017.
 */

@Transactional
@Aspect
public class NotificationAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    private CNotificationService cNotificationService;

    @Around(value="execution(public * com.firefly.conoche.repository.notifications.NotifyRepository+.save(..)) && args(notificable,..) && target(repo)")
    public <T extends IEntity> Object audit(ProceedingJoinPoint pjp, T notificable, NotifyRepository<T> repo) throws Throwable {

        if(notificable.getId() == null) {
            T afther = (T) pjp.proceed();
            createNotifications(repo, afther, Stream.empty(), ActionType.CREATE);
            return afther;
        }

        Map<String, Object> beforeMap = Optional
            .ofNullable(repo.findOne(notificable.getId()))
            .map(IEntity::notificationInfo).orElse(null);

        T afther = (T) pjp.proceed();

        Stream<String> changes = getChanges(beforeMap,
            afther.notificationInfo());
        createNotifications(repo, afther,
            changes, ActionType.UPDATE);
        return afther;
    }


    public <T extends IEntity> void createNotifications(NotifyRepository<T> repo,
                                                        T entity,
                                                        Stream<String> changes,
                                                        ActionType action) {
        cNotificationService.createNotifications(
            changes,
            () -> repo.notificationRecipients(entity),
            entity.getId(),
            entity.getObjectType(),
            action);
    }

    public Stream<String> getChanges(Map<String, Object> beforeMap,
                                  Map<String, Object> aftherMap) {
       return aftherMap.entrySet()
            .stream()
            .filter(e ->
                isDiferent (e.getValue(),
                    beforeMap.get(e.getKey())))
            .map(Map.Entry::getKey);
    }


    public boolean isDiferent(Object o1, Object o2)  {
        boolean isArray = o1.getClass().isArray();
        return (isArray && isDiferentArray(o1, o2))
            || (!isArray && !Objects.equals(o1, o2));
    }
    private boolean isDiferentArray(Object o1, Object o2) {
        int length = Array.getLength(o1);
        if (Array.getLength(o2) != length) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if(!Objects.equals(Array.get(o1, i), Array.get(o2, i))){
                return true;
            }
        }
        return false;
    }

}
