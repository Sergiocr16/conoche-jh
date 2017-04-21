package com.firefly.conoche.repository.notifications;

import com.firefly.conoche.domain.User;
import com.firefly.conoche.domain.interfaces.IEntity;

import java.io.Serializable;
import java.util.*;

/**
 * Created by melvin on 4/20/2017.
 */

public class NotificationRepositoryImpl<T extends IEntity, ID extends Serializable> implements NotificationRepositoryCustom<T, ID> {
    @Override
    public Set<User> notificationRecipients(T entity) {
        return new HashSet<>();
    }
}
