package com.firefly.conoche.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Promotion entity.
 */
public class PromotionDTO implements Serializable {

    private Long id;

    @NotNull
    private String description;

    private String imageUrl;

    @Lob
    private byte[] image;
    private String imageContentType;

    @NotNull
    private ZonedDateTime initialTime;

    @NotNull
    private ZonedDateTime finalTime;

    @NotNull
    @Min(value = 1)
    private Integer maximumCodePerUser;

    private Long eventId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }
    public ZonedDateTime getInitialTime() {
        return initialTime;
    }

    public void setInitialTime(ZonedDateTime initialTime) {
        this.initialTime = initialTime;
    }
    public ZonedDateTime getFinalTime() {
        return finalTime;
    }

    public void setFinalTime(ZonedDateTime finalTime) {
        this.finalTime = finalTime;
    }
    public Integer getMaximumCodePerUser() {
        return maximumCodePerUser;
    }

    public void setMaximumCodePerUser(Integer maximumCodePerUser) {
        this.maximumCodePerUser = maximumCodePerUser;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PromotionDTO promotionDTO = (PromotionDTO) o;

        if ( ! Objects.equals(id, promotionDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PromotionDTO{" +
            "id=" + id +
            ", description='" + description + "'" +
            ", imageUrl='" + imageUrl + "'" +
            ", image='" + image + "'" +
            ", initialTime='" + initialTime + "'" +
            ", finalTime='" + finalTime + "'" +
            ", maximumCodePerUser='" + maximumCodePerUser + "'" +
            '}';
    }
}
