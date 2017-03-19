package com.firefly.conoche.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A RealTimeEventImage.
 */
@Entity
@Table(name = "real_time_event_image")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RealTimeEventImage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @NotNull
    @Column(name = "creation_time", nullable = false)
    private ZonedDateTime creationTime;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "aspect_ratio", nullable = false)
    private Double aspectRatio;

    @Size(max = 55)
    @Column(name = "description", length = 55)
    private String description;

    @ManyToOne
    private Event event;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public RealTimeEventImage imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ZonedDateTime getCreationTime() {
        return creationTime;
    }

    public RealTimeEventImage creationTime(ZonedDateTime creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    public void setCreationTime(ZonedDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public Double getAspectRatio() {
        return aspectRatio;
    }

    public RealTimeEventImage aspectRatio(Double aspectRatio) {
        this.aspectRatio = aspectRatio;
        return this;
    }

    public void setAspectRatio(Double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public String getDescription() {
        return description;
    }

    public RealTimeEventImage description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Event getEvent() {
        return event;
    }

    public RealTimeEventImage event(Event event) {
        this.event = event;
        return this;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RealTimeEventImage realTimeEventImage = (RealTimeEventImage) o;
        if (realTimeEventImage.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, realTimeEventImage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RealTimeEventImage{" +
            "id=" + id +
            ", imageUrl='" + imageUrl + "'" +
            ", creationTime='" + creationTime + "'" +
            ", aspectRatio='" + aspectRatio + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
