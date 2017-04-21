package com.firefly.conoche.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.*;
import javax.persistence.Lob;

/**
 * A DTO for the Event entity.
 */
public class EventDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Lob
    private String details;

    @NotNull
    private Double price;

    @Lob
    private byte[] banner;
    private String bannerContentType;

    private String bannerUrl;

    @NotNull
    private ZonedDateTime initialTime;

    @NotNull
    private ZonedDateTime finalTime;

    private Set<UserDTO> attendingUsers = new HashSet<>();

    private Set<ServicioDTO> services = new HashSet<>();

    private Long localId;

    private String localName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    public byte[] getBanner() {
        return banner;
    }

    public void setBanner(byte[] banner) {
        this.banner = banner;
    }

    public String getBannerContentType() {
        return bannerContentType;
    }

    public void setBannerContentType(String bannerContentType) {
        this.bannerContentType = bannerContentType;
    }
    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
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

    public Set<UserDTO> getAttendingUsers() {
        return attendingUsers;
    }

    public void setAttendingUsers(Set<UserDTO> users) {
        this.attendingUsers = users;
    }

    public Set<ServicioDTO> getServices() {
        return services;
    }

    public void setServices(Set<ServicioDTO> servicios) {
        this.services = servicios;
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

        EventDTO eventDTO = (EventDTO) o;

        if ( ! Objects.equals(id, eventDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EventDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", details='" + details + "'" +
            ", price='" + price + "'" +
            ", banner='" + banner + "'" +
            ", bannerUrl='" + bannerUrl + "'" +
            ", initialTime='" + initialTime + "'" +
            ", finalTime='" + finalTime + "'" +
            '}';
    }
}
