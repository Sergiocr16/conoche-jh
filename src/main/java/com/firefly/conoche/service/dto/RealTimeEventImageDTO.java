package com.firefly.conoche.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the RealTimeEventImage entity.
 */
public class RealTimeEventImageDTO implements Serializable {

    private Long id;

    private String imageUrl;

    @NotNull
    private ZonedDateTime creationTime;

    @NotNull
    private Integer width;

    @NotNull
    private Integer height;

    private String description;

    private Long eventId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public ZonedDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(ZonedDateTime creationTime) {
        this.creationTime = creationTime;
    }
    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

        RealTimeEventImageDTO realTimeEventImageDTO = (RealTimeEventImageDTO) o;

        if ( ! Objects.equals(id, realTimeEventImageDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RealTimeEventImageDTO{" +
            "id=" + id +
            ", imageUrl='" + imageUrl + "'" +
            ", creationTime='" + creationTime + "'" +
            ", width='" + width + "'" +
            ", height='" + height + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
