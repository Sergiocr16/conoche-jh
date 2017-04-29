package com.firefly.conoche.service.aspects;

import com.firefly.conoche.domain.enumeration.ActionObjectType;
import com.firefly.conoche.domain.enumeration.ActionType;
import com.firefly.conoche.domain.interfaces.IEntity;
import com.firefly.conoche.repository.notifications.NotifyRepository;
import com.firefly.conoche.service.customService.CNotificationService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import javax.inject.Inject;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;



/**
 * Created by melvin on 4/19/2017.
 * Aspecto que se utiliza para interceptar diversas operaciones de los repositorios
 */

@Transactional
@Aspect
public class NotificationAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    private CNotificationService cNotificationService;

    /**
     * Advice para interseptar el guardar y editar. Crea notificaciones para cada recipiente.
     * @param pjp metodo que se intercepta.
     * @param notificable Entidad por eliminar o editar.
     * @param repo repositorio al cual se intercepta.
     * @param <T> Cualquier tipo que implemente IEntitu.
     * @return Objecto retornado por el metodo a interceptar.
     * @throws Throwable
     */
    @Around(value="execution(public * com.firefly.conoche.repository.notifications.NotifyRepository+.save(..)) && args(notificable,..) && target(repo)")
    public <T extends IEntity> Object auditCreate(ProceedingJoinPoint pjp, T notificable, NotifyRepository<T> repo) throws Throwable {

        if(notificable.getId() == null) {
            T afther = (T) pjp.proceed();
            sendNotifications(repo, afther, Stream.empty(), ActionType.CREATE);
            return afther;
        }

        Map<String, Object> beforeMap = Optional
            .ofNullable(repo.findOne(notificable.getId()))
            .map(IEntity::notificationInfo).orElse(null);

        T afther = (T) pjp.proceed();

        Stream<String> changes = getChanges(beforeMap,
            afther.notificationInfo());
        sendNotifications(repo, afther,
            changes, ActionType.UPDATE);



        return afther;
    }

    /**
     * Advice para interceptar el borrar de un repositorio.
     * @param pjp join point
     * @param id id de la entidad
     * @param repo repositorio.
     * @param <T>
     * @throws Throwable
     */
    @Around(value="execution(public * com.firefly.conoche.repository.notifications.NotifyRepository+.delete(..)) && args(id,..) && target(repo)")
    public <T extends IEntity> void auditDelete(ProceedingJoinPoint pjp, Long id, NotifyRepository<T> repo) throws Throwable {

        T entity = repo.findOne(id);
        pjp.proceed();
        cNotificationService.deactivateActionObjects(
            entity.getObjectType(), Stream.of(entity.getId()));
    }

    /**
     * Advice Advice para interceptar el borrar de un repositorio
     * @param entity
     * @param <T>
     * @throws Throwable
     */
    @AfterReturning(pointcut="execution(public * com.firefly.conoche.repository.notifications.NotifyRepository+.delete(..)) && args(entity,..)")
    public <T extends IEntity> void auditDeleteEntity(T entity) throws Throwable {
        cNotificationService.deactivateActionObjects(
            entity.getObjectType(), Stream.of(entity.getId()));
    }

    /**
     * Advice para interceptar la auditoria.
     * @param entitys
     * @param <T>
     * @throws Throwable
     */
    @AfterReturning(pointcut="execution(public * com.firefly.conoche.repository.notifications.NotifyRepository+.deleteInBatch(..))" +
        " && args(entitys,..)")
    public <T extends IEntity> void auditDeleteEntitys(Iterable<T> entitys) throws Throwable {
        toStream(entitys)
            .map(T::getObjectType)
            .findAny()
            .ifPresent( t -> {
                Stream<Long> stream = toStream(entitys)
                    .map(T::getId);
                cNotificationService.deactivateActionObjects(t, stream);
            });
    }


    private <T> Stream<T> toStream(Iterable<T> itr) {
        return StreamSupport.stream(itr.spliterator(), false);
    }

    private <T extends IEntity> void sendNotifications(NotifyRepository<T> repo,
                                                        T entity,
                                                        Stream<String> changes,
                                                        ActionType action) throws InterruptedException, ExecutionException {
        cNotificationService.createNotifications(
            changes,
            () -> repo.notificationRecipients(entity),
            entity.getId(),
            entity.getObjectType(),
            action);
    }

    private Stream<String> getChanges(Map<String, Object> beforeMap,
                                  Map<String, Object> aftherMap) {
       return aftherMap.entrySet()
            .stream()
            .filter(e ->
                isDiferent (e.getValue(),
                    beforeMap.get(e.getKey())))
            .map(Map.Entry::getKey);
    }


    private boolean isDiferent(Object o1, Object o2)  {
        return o1 != o2 &&
            ((o1 == null || o2 == null )
            || (o1.getClass().isArray() && isDiferentArray(o1, o2))
            || (!o1.getClass().isArray() && !Objects.equals(o1, o2)));
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
