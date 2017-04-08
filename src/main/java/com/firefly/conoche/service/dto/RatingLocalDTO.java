package com.firefly.conoche.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the RatingLocal entity.
 */
public class RatingLocalDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 0)
    @Max(value = 10)
    private Integer rating;

    private String description;


    private String userLogin;


    private ZonedDateTime creationDate;

    private Long userDetailsId;

    private Long localId;

    private String localName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getUserDetailsId() {
        return userDetailsId;
    }

    public void setUserDetailsId(Long userId) {
        this.userDetailsId = userId;
    }

    public Long getLocalId() {
        return localId;
    }

    public void setLocalId(Long localId) {
        this.localId = localId;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RatingLocalDTO ratingLocalDTO = (RatingLocalDTO) o;

        if ( ! Objects.equals(id, ratingLocalDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RatingLocalDTO{" +
            "id=" + id +
            ", rating='" + rating + "'" +
            ", description='" + description + "'" +
            ", userLogin='" + userLogin + "'" +
            ", creationDate='" + creationDate + "'" +
            '}';
    }
}
