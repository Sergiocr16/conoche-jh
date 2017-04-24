package com.firefly.conoche.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
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

    @NotNull
    @Column(name = "creation_time", nullable = false)
    private ZonedDateTime creationTime;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @OneToMany(mappedBy = "actionObject")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ObjectChange> changes = new HashSet<>();

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

    public ZonedDateTime getCreationTime() {
        return creationTime;
    }

    public ActionObject creationTime(ZonedDateTime creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    public void setCreationTime(ZonedDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public Boolean isActive() {
        return active;
    }

    public ActionObject active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<ObjectChange> getChanges() {
        return changes;
    }

    public ActionObject changes(Set<ObjectChange> objectChanges) {
        this.changes = objectChanges;
        return this;
    }

    public ActionObject addChanges(ObjectChange objectChange) {
        this.changes.add(objectChange);
        objectChange.setActionObject(this);
        return this;
    }

    public ActionObject removeChanges(ObjectChange objectChange) {
        this.changes.remove(objectChange);
        objectChange.setActionObject(null);
        return this;
    }

    public void setChanges(Set<ObjectChange> objectChanges) {
        this.changes = objectChanges;
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
            ", creationTime='" + creationTime + "'" +
            ", active='" + active + "'" +
            '}';
    }
}
