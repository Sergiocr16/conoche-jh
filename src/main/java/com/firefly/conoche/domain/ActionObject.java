package com.firefly.conoche.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.firefly.conoche.domain.enumeration.ActionType;

import com.firefly.conoche.domain.enumeration.ActionObjectType;

/**
 * A ActionObject.
 */
@Entity
@Table(name = "action_object")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ActionObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "object_id", nullable = false)
    private Long objectId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type")
    private ActionType actionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "object_type")
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

    public ActionObject objectId(Long objectId) {
        this.objectId = objectId;
        return this;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public ActionObject actionType(ActionType actionType) {
        this.actionType = actionType;
        return this;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public ActionObjectType getObjectType() {
        return objectType;
    }

    public ActionObject objectType(ActionObjectType objectType) {
        this.objectType = objectType;
        return this;
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
        ActionObject actionObject = (ActionObject) o;
        if (actionObject.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, actionObject.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ActionObject{" +
            "id=" + id +
            ", objectId='" + objectId + "'" +
            ", actionType='" + actionType + "'" +
            ", objectType='" + objectType + "'" +
            '}';
    }
}
