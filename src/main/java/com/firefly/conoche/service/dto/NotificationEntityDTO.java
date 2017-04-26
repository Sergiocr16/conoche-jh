package com.firefly.conoche.service.dto;

import com.firefly.conoche.domain.ActionObject;
import com.firefly.conoche.domain.enumeration.ActionObjectType;

import java.io.Serializable;

/**
 * Created by melvin on 4/24/2017.
 */
public class NotificationEntityDTO implements Serializable {

    private Long id;
    private ActionObjectType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ActionObjectType getType() {
        return type;
    }

    public void setType(ActionObjectType type) {
        this.type = type;
    }
}
