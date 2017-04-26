package com.firefly.conoche.domain.interfaces;

import com.firefly.conoche.domain.enumeration.ActionObjectType;

import java.util.Map;
import java.util.Set;

/**
 * Created by melvin on 4/20/2017.
 */
public interface IEntity {
    Long getId();
    ActionObjectType getObjectType();
    Map<String, Object> notificationInfo();

}
