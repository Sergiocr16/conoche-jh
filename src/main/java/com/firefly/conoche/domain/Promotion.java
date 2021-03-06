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
 * A Promotion.
 */
@Entity
@Table(name = "promotion")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Promotion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @NotNull
    @Column(name = "initial_time", nullable = false)
    private ZonedDateTime initialTime;

    @NotNull
    @Column(name = "final_time", nullable = false)
    private ZonedDateTime finalTime;

    @NotNull
    @Min(value = 1)
    @Column(name = "maximum_code_per_user", nullable = false)
    private Integer maximumCodePerUser;

    @OneToMany(mappedBy = "promotion")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PromotionCode> codes = new HashSet<>();

    @ManyToOne
    private Event event;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Promotion description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Promotion imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public byte[] getImage() {
        return image;
    }

    public Promotion image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public Promotion imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public ZonedDateTime getInitialTime() {
        return initialTime;
    }

    public Promotion initialTime(ZonedDateTime initialTime) {
        this.initialTime = initialTime;
        return this;
    }

    public void setInitialTime(ZonedDateTime initialTime) {
        this.initialTime = initialTime;
    }

    public ZonedDateTime getFinalTime() {
        return finalTime;
    }

    public Promotion finalTime(ZonedDateTime finalTime) {
        this.finalTime = finalTime;
        return this;
    }

    public void setFinalTime(ZonedDateTime finalTime) {
        this.finalTime = finalTime;
    }

    public Integer getMaximumCodePerUser() {
        return maximumCodePerUser;
    }

    public Promotion maximumCodePerUser(Integer maximumCodePerUser) {
        this.maximumCodePerUser = maximumCodePerUser;
        return this;
    }

    public void setMaximumCodePerUser(Integer maximumCodePerUser) {
        this.maximumCodePerUser = maximumCodePerUser;
    }

    public Set<PromotionCode> getCodes() {
        return codes;
    }

    public Promotion codes(Set<PromotionCode> promotionCodes) {
        this.codes = promotionCodes;
        return this;
    }

    public Promotion addCodes(PromotionCode promotionCode) {
        this.codes.add(promotionCode);
        promotionCode.setPromotion(this);
        return this;
    }

    public Promotion removeCodes(PromotionCode promotionCode) {
        this.codes.remove(promotionCode);
        promotionCode.setPromotion(null);
        return this;
    }

    public void setCodes(Set<PromotionCode> promotionCodes) {
        this.codes = promotionCodes;
    }

    public Event getEvent() {
        return event;
    }

    public Promotion event(Event event) {
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
        Promotion promotion = (Promotion) o;
        if (promotion.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, promotion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Promotion{" +
            "id=" + id +
            ", description='" + description + "'" +
            ", imageUrl='" + imageUrl + "'" +
            ", image='" + image + "'" +
            ", imageContentType='" + imageContentType + "'" +
            ", initialTime='" + initialTime + "'" +
            ", finalTime='" + finalTime + "'" +
            ", maximumCodePerUser='" + maximumCodePerUser + "'" +
            '}';
    }
}
