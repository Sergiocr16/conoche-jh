package com.firefly.conoche.repository.customRepository;

import com.firefly.conoche.domain.Notification;
import com.firefly.conoche.domain.enumeration.Provincia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

/**
 * Created by melvin on 4/22/2017.
 */

public interface CNotificationRepository extends JpaRepository<Notification,Long> {
    @Query("select notification from Notification notification " +
        "where notification.user.login = ?#{principal.username} " +
        "and (:isRead is null or notification.isRead = :isRead)")
    Page<Notification> findByUserIsCurrentUser(Pageable page, @Param("isRead") Boolean isRead);


}
