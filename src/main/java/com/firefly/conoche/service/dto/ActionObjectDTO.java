package com.firefly.conoche.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.firefly.conoche.domain.enumeration.ActionType;
import com.firefly.conoche.domain.enumeration.ActionObjectType;

/**
 * A DTO for the ActionObject entity.
 */
public class ActionObjectDTO implements Serializable {

    private Long id;

    @NotNull
    private Long objectId;

    private ActionType actionType;

    private ActionObjectType objectType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }
    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }
    public ActionObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ActionObjectType objectType) {
        this.objectType = objectType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ActionObjectDTO actionObjectDTO = (ActionObjectDTO) o;

        if ( ! Objects.equals(id, actionObjectDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ActionObjectDTO{" +
            "id=" + id +
            ", objectId='" + objectId + "'" +
            ", actionType='" + actionType + "'" +
            ", objectType='" + objectType + "'" +
            '}';
    }
}
