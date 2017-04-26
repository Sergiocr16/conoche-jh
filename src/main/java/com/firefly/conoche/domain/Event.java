package com.firefly.conoche.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.firefly.conoche.domain.enumeration.ActionObjectType;
import com.firefly.conoche.domain.interfaces.IEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * A Event.
 */
@Entity
@Table(name = "event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Event implements Serializable, IEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Lob
    @Column(name = "details", nullable = false)
    private String details;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @Lob
    @Column(name = "banner")
    private byte[] banner;

    @Column(name = "banner_content_type")
    private String bannerContentType;

    @Column(name = "banner_url")
    private String bannerUrl;

    @NotNull
    @Column(name = "initial_time", nullable = false)
    private ZonedDateTime initialTime;

    @NotNull
    @Column(name = "final_time", nullable = false)
    private ZonedDateTime finalTime;

    @OneToMany(mappedBy = "event")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Promotion> promotions = new HashSet<>();

    @OneToMany(mappedBy = "event")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EventImage> images = new HashSet<>();

    @OneToMany(mappedBy = "event")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RealTimeEventImage> realTimeImages = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "event_attending_users",
               joinColumns = @JoinColumn(name="events_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="attending_users_id", referencedColumnName="id"))
    private Set<User> attendingUsers = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "event_services",
               joinColumns = @JoinColumn(name="events_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="services_id", referencedColumnName="id"))
    private Set<Servicio> services = new HashSet<>();

    @ManyToOne
    private Local local;

    @OneToMany(mappedBy = "event")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Message> messages = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Event name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public Event details(String details) {
        this.details = details;
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Double getPrice() {
        return price;
    }

    public Event price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public byte[] getBanner() {
        return banner;
    }

    public Event banner(byte[] banner) {
        this.banner = banner;
        return this;
    }

    public void setBanner(byte[] banner) {
        this.banner = banner;
    }

    public String getBannerContentType() {
        return bannerContentType;
    }

    public Event bannerContentType(String bannerContentType) {
        this.bannerContentType = bannerContentType;
        return this;
    }

    public void setBannerContentType(String bannerContentType) {
        this.bannerContentType = bannerContentType;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public Event bannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
        return this;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public ZonedDateTime getInitialTime() {
        return initialTime;
    }

    public Event initialTime(ZonedDateTime initialTime) {
        this.initialTime = initialTime;
        return this;
    }

    public void setInitialTime(ZonedDateTime initialTime) {
        this.initialTime = initialTime;
    }

    public ZonedDateTime getFinalTime() {
        return finalTime;
    }

    public Event finalTime(ZonedDateTime finalTime) {
        this.finalTime = finalTime;
        return this;
    }

    public void setFinalTime(ZonedDateTime finalTime) {
        this.finalTime = finalTime;
    }

    public Set<Promotion> getPromotions() {
        return promotions;
    }

    public Event promotions(Set<Promotion> promotions) {
        this.promotions = promotions;
        return this;
    }

    public Event addPromotions(Promotion promotion) {
        this.promotions.add(promotion);
        promotion.setEvent(this);
        return this;
    }

    public Event removePromotions(Promotion promotion) {
        this.promotions.remove(promotion);
        promotion.setEvent(null);
        return this;
    }

    public void setPromotions(Set<Promotion> promotions) {
        this.promotions = promotions;
    }

    public Set<EventImage> getImages() {
        return images;
    }

    public Event images(Set<EventImage> eventImages) {
        this.images = eventImages;
        return this;
    }

    public Event addImages(EventImage eventImage) {
        this.images.add(eventImage);
        eventImage.setEvent(this);
        return this;
    }

    public Event removeImages(EventImage eventImage) {
        this.images.remove(eventImage);
        eventImage.setEvent(null);
        return this;
    }

    public void setImages(Set<EventImage> eventImages) {
        this.images = eventImages;
    }

    public Set<RealTimeEventImage> getRealTimeImages() {
        return realTimeImages;
    }

    public Event realTimeImages(Set<RealTimeEventImage> realTimeEventImages) {
        this.realTimeImages = realTimeEventImages;
        return this;
    }

    public Event addRealTimeImages(RealTimeEventImage realTimeEventImage) {
        this.realTimeImages.add(realTimeEventImage);
        realTimeEventImage.setEvent(this);
        return this;
    }

    public Event removeRealTimeImages(RealTimeEventImage realTimeEventImage) {
        this.realTimeImages.remove(realTimeEventImage);
        realTimeEventImage.setEvent(null);
        return this;
    }

    public void setRealTimeImages(Set<RealTimeEventImage> realTimeEventImages) {
        this.realTimeImages = realTimeEventImages;
    }

    public Set<User> getAttendingUsers() {
        return attendingUsers;
    }

    public Event attendingUsers(Set<User> users) {
        this.attendingUsers = users;
        return this;
    }

    public Event addAttendingUsers(User user) {
        this.attendingUsers.add(user);
        return this;
    }

    public Event removeAttendingUsers(User user) {
        this.attendingUsers.remove(user);
        return this;
    }

    public void setAttendingUsers(Set<User> users) {
        this.attendingUsers = users;
    }

    public Set<Servicio> getServices() {
        return services;
    }

    public Event services(Set<Servicio> servicios) {
        this.services = servicios;
        return this;
    }

    public Event addServices(Servicio servicio) {
        this.services.add(servicio);
        return this;
    }

    public Event removeServices(Servicio servicio) {
        this.services.remove(servicio);
        return this;
    }

    public void setServices(Set<Servicio> servicios) {
        this.services = servicios;
    }

    public Local getLocal() {
        return local;
    }

    public Event local(Local local) {
        this.local = local;
        return this;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public Event messages(Set<Message> messages) {
        this.messages = messages;
        return this;
    }

    public Event addMessages(Message message) {
        this.messages.add(message);
        message.setEvent(this);
        return this;
    }

    public Event removeMessages(Message message) {
        this.messages.remove(message);
        message.setEvent(null);
        return this;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        if (event.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Event{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", details='" + details + "'" +
            ", price='" + price + "'" +
            ", banner='" + banner + "'" +
            ", bannerContentType='" + bannerContentType + "'" +
            ", bannerUrl='" + bannerUrl + "'" +
            ", initialTime='" + initialTime + "'" +
            ", finalTime='" + finalTime + "'" +
            '}';
    }

    @Override
    public Map<String, Object> notificationInfo() {

        long[] servicesArray = services.stream()
            .mapToLong(Servicio::getId)
            .sorted()
            .toArray();

        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("details", details);
        map.put("price", price);
        map.put("initialTime", initialTime.toEpochSecond());
        map.put("finalTime", finalTime.toEpochSecond());
        map.put("banner", banner);
        map.put("services",  servicesArray);


        return map;
    }

    @Override
    public ActionObjectType getObjectType() {
        return ActionObjectType.EVENT;
    }
}
