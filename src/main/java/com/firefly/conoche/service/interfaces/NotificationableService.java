package com.firefly.conoche.service.interfaces;

import com.firefly.conoche.service.dto.EventDTO;

/**
 * Created by melvin on 4/19/2017.
 */
public interface NotificationableService<T extends Notificationable> {
    T save(T element);
    T findOne(Long id);
}
