package com.firefly.conoche.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A RatingLocal.
 */
@Entity
@Table(name = "rating_local")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RatingLocal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(value = 0)
    @Max(value = 10)
    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "user_login", nullable = false)
    private String userLogin;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private ZonedDateTime creationDate;

    @ManyToOne
    private User userDetails;

    @ManyToOne
    private Local local;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public RatingLocal rating(Integer rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public RatingLocal description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public RatingLocal userLogin(String userLogin) {
        this.userLogin = userLogin;
        return this;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public RatingLocal creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public User getUserDetails() {
        return userDetails;
    }

    public RatingLocal userDetails(User user) {
        this.userDetails = user;
        return this;
    }

    public void setUserDetails(User user) {
        this.userDetails = user;
    }

    public Local getLocal() {
        return local;
    }

    public RatingLocal local(Local local) {
        this.local = local;
        return this;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RatingLocal ratingLocal = (RatingLocal) o;
        if (ratingLocal.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, ratingLocal.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RatingLocal{" +
            "id=" + id +
            ", rating='" + rating + "'" +
            ", description='" + description + "'" +
            ", userLogin='" + userLogin + "'" +
            ", creationDate='" + creationDate + "'" +
            '}';
    }
}
