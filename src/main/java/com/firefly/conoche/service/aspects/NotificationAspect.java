package com.firefly.conoche.service.aspects;

import com.firefly.conoche.service.interfaces.Notificationable;
import com.firefly.conoche.service.interfaces.NotificationableService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Created by melvin on 4/19/2017.
 */
@Aspect
public class NotificationAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Around(value="execution(public * com.firefly.conoche.service.interfaces.NotificationableService+.save(..)) && args(notificable,..)")
    public <T extends Notificationable> void audit(ProceedingJoinPoint pjp, Notificationable notificable) throws Throwable {
        Notificationable before = ((NotificationableService<T>)pjp
            .getTarget())
            .findOne(notificable.getId());

        //before

        Notificationable afther = (Notificationable) pjp.proceed();

        //afther

        Map<String, Object> beforeMap = before.getNotificationInfo();
        Map<String, Object> aftherMap = afther.getNotificationInfo();
        aftherMap.entrySet()
                .stream()
                .filter(e ->
                    !Objects.equals(e.getValue(),
                        beforeMap.get(e.getKey())))
                .map(Object::toString)
                .forEach(log::error);
    }


    public static <T> List<Field> compare(T o1, T o2) throws IllegalAccessException {

        List<Field> differentFields = new ArrayList<>();
        if(o1 == o2) {
            return differentFields;
        }
        Class clazz = o1.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for(Field f: fields){
            boolean accessible = f.isAccessible();
            if(! accessible){
                f.setAccessible(true);
            }
            Object o1f = f.get(o1);
            Object o2f = f.get(o2);
            if (f.getType().isArray()) {

            }
            if((f.getType().isArray() && isDiferentArray(o1f, o2f))
                || !Objects.equals(o1f, o2f)){
                differentFields.add(f);
            }
            f.setAccessible(accessible);
        }
        return differentFields;
    }

    private static boolean isDiferentArray(Object o1, Object o2) {
        int length = Array.getLength(o1);
        if (Array.getLength(o2) != length) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if(Array.get(o1, i) != Array.get(o2, i)){
                return true;
            }
        }
        return false;
    }

}
