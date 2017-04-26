package com.firefly.conoche.repository.notifications;

import com.firefly.conoche.domain.Authority;
import com.firefly.conoche.domain.interfaces.IEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Created by melvin on 4/20/2017.
 */

@NoRepositoryBean
public interface NotifyRepository<T extends IEntity> extends JpaRepository<T, Long>, NotificationRepositoryCustom<T, Long> {

}

