package com.firefly.conoche.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.firefly.conoche.domain.enumeration.Provincia;

/**
 * A Local.
 */
@Entity
@Table(name = "local")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Local implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "longitud")
    private Double longitud;

    @Lob
    @Column(name = "banner")
    private byte[] banner;

    @Column(name = "banner_content_type")
    private String bannerContentType;

    @Column(name = "banner_url")
    private String bannerUrl;

    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "descripcion")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "provincia")
    private Provincia provincia;

    @Column(name = "rating")
    private Double rating;

    @OneToMany(mappedBy = "local")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ImagenLocal> images = new HashSet<>();

    @OneToMany(mappedBy = "local")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Event> events = new HashSet<>();

    @OneToMany(mappedBy = "local")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Schedule> schedules = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "local_services",
               joinColumns = @JoinColumn(name="locals_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="services_id", referencedColumnName="id"))
    private Set<Servicio> services = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "local_subcribers",
               joinColumns = @JoinColumn(name="locals_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="subcribers_id", referencedColumnName="id"))
    private Set<User> subcribers = new HashSet<>();

    @OneToMany(mappedBy = "local")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RatingLocal> ratings = new HashSet<>();

    @ManyToOne
    private Category localCategory;

    @ManyToOne(optional = false)
    @NotNull
    private User owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Local name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLongitud() {
        return longitud;
    }

    public Local longitud(Double longitud) {
        this.longitud = longitud;
        return this;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public byte[] getBanner() {
        return banner;
    }

    public Local banner(byte[] banner) {
        this.banner = banner;
        return this;
    }

    public void setBanner(byte[] banner) {
        this.banner = banner;
    }

    public String getBannerContentType() {
        return bannerContentType;
    }

    public Local bannerContentType(String bannerContentType) {
        this.bannerContentType = bannerContentType;
        return this;
    }

    public void setBannerContentType(String bannerContentType) {
        this.bannerContentType = bannerContentType;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public Local bannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
        return this;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public Double getLatitud() {
        return latitud;
    }

    public Local latitud(Double latitud) {
        this.latitud = latitud;
        return this;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Local descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public Local provincia(Provincia provincia) {
        this.provincia = provincia;
        return this;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public Double getRating() {
        return rating;
    }

    public Local rating(Double rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Set<ImagenLocal> getImages() {
        return images;
    }

    public Local images(Set<ImagenLocal> imagenLocals) {
        this.images = imagenLocals;
        return this;
    }

    public Local addImages(ImagenLocal imagenLocal) {
        this.images.add(imagenLocal);
        imagenLocal.setLocal(this);
        return this;
    }

    public Local removeImages(ImagenLocal imagenLocal) {
        this.images.remove(imagenLocal);
        imagenLocal.setLocal(null);
        return this;
    }

    public void setImages(Set<ImagenLocal> imagenLocals) {
        this.images = imagenLocals;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public Local events(Set<Event> events) {
        this.events = events;
        return this;
    }

    public Local addEvents(Event event) {
        this.events.add(event);
        event.setLocal(this);
        return this;
    }

    public Local removeEvents(Event event) {
        this.events.remove(event);
        event.setLocal(null);
        return this;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Set<Schedule> getSchedules() {
        return schedules;
    }

    public Local schedules(Set<Schedule> schedules) {
        this.schedules = schedules;
        return this;
    }

    public Local addSchedules(Schedule schedule) {
        this.schedules.add(schedule);
        schedule.setLocal(this);
        return this;
    }

    public Local removeSchedules(Schedule schedule) {
        this.schedules.remove(schedule);
        schedule.setLocal(null);
        return this;
    }

    public void setSchedules(Set<Schedule> schedules) {
        this.schedules = schedules;
    }

    public Set<Servicio> getServices() {
        return services;
    }

    public Local services(Set<Servicio> servicios) {
        this.services = servicios;
        return this;
    }

    public Local addServices(Servicio servicio) {
        this.services.add(servicio);
        return this;
    }

    public Local removeServices(Servicio servicio) {
        this.services.remove(servicio);
        return this;
    }

    public void setServices(Set<Servicio> servicios) {
        this.services = servicios;
    }

    public Set<User> getSubcribers() {
        return subcribers;
    }

    public Local subcribers(Set<User> users) {
        this.subcribers = users;
        return this;
    }

    public Local addSubcribers(User user) {
        this.subcribers.add(user);
        return this;
    }

    public Local removeSubcribers(User user) {
        this.subcribers.remove(user);
        return this;
    }

    public void setSubcribers(Set<User> users) {
        this.subcribers = users;
    }

    public Set<RatingLocal> getRatings() {
        return ratings;
    }

    public Local ratings(Set<RatingLocal> ratingLocals) {
        this.ratings = ratingLocals;
        return this;
    }

    public Local addRatings(RatingLocal ratingLocal) {
        this.ratings.add(ratingLocal);
        ratingLocal.setLocal(this);
        return this;
    }

    public Local removeRatings(RatingLocal ratingLocal) {
        this.ratings.remove(ratingLocal);
        ratingLocal.setLocal(null);
        return this;
    }

    public void setRatings(Set<RatingLocal> ratingLocals) {
        this.ratings = ratingLocals;
    }

    public Category getLocalCategory() {
        return localCategory;
    }

    public Local localCategory(Category category) {
        this.localCategory = category;
        return this;
    }

    public void setLocalCategory(Category category) {
        this.localCategory = category;
    }

    public User getOwner() {
        return owner;
    }

    public Local owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Local local = (Local) o;
        if (local.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, local.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Local{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", longitud='" + longitud + "'" +
            ", banner='" + banner + "'" +
            ", bannerContentType='" + bannerContentType + "'" +
            ", bannerUrl='" + bannerUrl + "'" +
            ", latitud='" + latitud + "'" +
            ", descripcion='" + descripcion + "'" +
            ", provincia='" + provincia + "'" +
            ", rating='" + rating + "'" +
            '}';
    }
}
