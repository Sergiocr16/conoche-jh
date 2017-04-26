package com.firefly.conoche.repository.notifications;

import com.firefly.conoche.domain.User;
import com.firefly.conoche.domain.interfaces.IEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by melvin on 4/20/2017.
 */

@Transactional(readOnly = true)
public interface NotificationRepositoryCustom<T extends IEntity, ID extends Serializable> {
   Set<User> notificationRecipients(T entity);
}
