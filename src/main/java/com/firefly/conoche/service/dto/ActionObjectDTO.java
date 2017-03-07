package com.firefly.conoche.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.firefly.conoche.domain.enumeration.ActionObjectType;

/**
 * A DTO for the ActionObject entity.
 */
public class ActionObjectDTO implements Serializable {

    private Long id;

    @NotNull
    private Long objectId;

    private String description;

    private ActionObjectType objectType;

    private Long actionId;

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
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public ActionObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ActionObjectType objectType) {
        this.objectType = objectType;
    }

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
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
            ", description='" + description + "'" +
            ", objectType='" + objectType + "'" +
            '}';
    }
}
