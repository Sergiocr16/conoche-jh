package com.firefly.conoche.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.firefly.conoche.domain.enumeration.ActionObjectType;
import com.firefly.conoche.domain.interfaces.IEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * A Promotion.
 */
@Entity
@Table(name = "promotion")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Promotion implements Serializable, IEntity {

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

    @Column(name = "code_quantity")
    private Integer codeQuantity;

    @OneToMany(mappedBy = "promotion", cascade=CascadeType.ALL, orphanRemoval=true)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OnDelete(action = OnDeleteAction.CASCADE)
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

    public Integer getCodeQuantity() {
        return codeQuantity;
    }

    public Promotion codeQuantity(Integer codeQuantity) {
        this.codeQuantity = codeQuantity;
        return this;
    }

    public void setCodeQuantity(Integer codeQuantity) {
        this.codeQuantity = codeQuantity;
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
    /**
     *  author Sergio Casto
     *  Generate the promotions codes in base of the code quantity
     */
    public void generatePromotionsCodes(){
    for (int i = 0;i<this.codeQuantity;i++){
        PromotionCode promotionCode = new PromotionCode();
        promotionCode.setCode(randomString(5));
        promotionCode.setActive(true);
        promotionCode.setPromotion(this);
        this.addCodes(promotionCode);
    }
    }


    private String randomString( int len ){
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString().toUpperCase();
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
            ", codeQuantity='" + codeQuantity + "'" +
            '}';
    }

    @Override
    public ActionObjectType getObjectType() {
        return ActionObjectType.PROMOTION;
    }

    @Override
    public Map<String, Object> notificationInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("description", description);
        map.put("image", image);
        map.put("initialTime", initialTime);
        map.put("finalTime", finalTime);
        map.put("codeQuantity", codeQuantity);

        return map;
    }
}
