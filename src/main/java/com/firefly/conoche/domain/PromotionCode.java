package com.firefly.conoche.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PromotionCode.
 */
@Entity
@Table(name = "promotion_code")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PromotionCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @ManyToOne
    private Promotion promotion;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public PromotionCode code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean isActive() {
        return active;
    }

    public PromotionCode active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public PromotionCode promotion(Promotion promotion) {
        this.promotion = promotion;
        return this;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public User getUser() {
        return user;
    }

    public PromotionCode user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PromotionCode promotionCode = (PromotionCode) o;
        if (promotionCode.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, promotionCode.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PromotionCode{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", active='" + active + "'" +
            '}';
    }
}
