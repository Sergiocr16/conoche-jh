package com.firefly.conoche.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.firefly.conoche.domain.ObjectChange;
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

    private Set<ObjectChangeDTO> changes;
    @NotNull
    private ZonedDateTime creationTime;

    @NotNull
    private Boolean active;

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
    public ZonedDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(ZonedDateTime creationTime) {
        this.creationTime = creationTime;
    }
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
            ", creationTime='" + creationTime + "'" +
            ", active='" + active + "'" +
            '}';
    }

    public Set<ObjectChangeDTO> getChanges() {
        return changes;
    }

    public void setChanges(Set<ObjectChangeDTO> changes) {
        this.changes = changes;
    }
}
