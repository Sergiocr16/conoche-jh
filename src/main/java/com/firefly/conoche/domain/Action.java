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

/**
 * A Action.
 */
@Entity
@Table(name = "action")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Action implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @NotNull
    @Column(name = "creation", nullable = false)
    private ZonedDateTime creation;

    @OneToMany(mappedBy = "action")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ActionObject> objects = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "action_users",
               joinColumns = @JoinColumn(name="actions_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="users_id", referencedColumnName="id"))
    private Set<User> users = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Action descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ZonedDateTime getCreation() {
        return creation;
    }

    public Action creation(ZonedDateTime creation) {
        this.creation = creation;
        return this;
    }

    public void setCreation(ZonedDateTime creation) {
        this.creation = creation;
    }

    public Set<ActionObject> getObjects() {
        return objects;
    }

    public Action objects(Set<ActionObject> actionObjects) {
        this.objects = actionObjects;
        return this;
    }

    public Action addObjects(ActionObject actionObject) {
        this.objects.add(actionObject);
        actionObject.setAction(this);
        return this;
    }

    public Action removeObjects(ActionObject actionObject) {
        this.objects.remove(actionObject);
        actionObject.setAction(null);
        return this;
    }

    public void setObjects(Set<ActionObject> actionObjects) {
        this.objects = actionObjects;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Action users(Set<User> users) {
        this.users = users;
        return this;
    }

    public Action addUsers(User user) {
        this.users.add(user);
        return this;
    }

    public Action removeUsers(User user) {
        this.users.remove(user);
        return this;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Action action = (Action) o;
        if (action.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, action.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Action{" +
            "id=" + id +
            ", descripcion='" + descripcion + "'" +
            ", creation='" + creation + "'" +
            '}';
    }
}
