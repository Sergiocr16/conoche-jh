package com.firefly.conoche.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;
import com.firefly.conoche.domain.enumeration.Provincia;

/**
 * A DTO for the Local entity.
 */
public class LocalDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Double longitud;

    @Lob
    private byte[] banner;
    private String bannerContentType;

    private String bannerUrl;

    private Double latitud;

    private String descripcion;

    private Provincia provincia;

    private Double rating;

    private Set<ServicioDTO> services = new HashSet<>();

    private Set<UserDTO> subcribers = new HashSet<>();

    private Long localCategoryId;

    private String localCategoryName;

    private Long ownerId;

    private String ownerLogin;

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
    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
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
    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }
    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Set<ServicioDTO> getServices() {
        return services;
    }

    public void setServices(Set<ServicioDTO> servicios) {
        this.services = servicios;
    }

    public Set<UserDTO> getSubcribers() {
        return subcribers;
    }

    public void setSubcribers(Set<UserDTO> users) {
        this.subcribers = users;
    }

    public Long getLocalCategoryId() {
        return localCategoryId;
    }

    public void setLocalCategoryId(Long categoryId) {
        this.localCategoryId = categoryId;
    }

    public String getLocalCategoryName() {
        return localCategoryName;
    }

    public void setLocalCategoryName(String categoryName) {
        this.localCategoryName = categoryName;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long userId) {
        this.ownerId = userId;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String userLogin) {
        this.ownerLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LocalDTO localDTO = (LocalDTO) o;

        if ( ! Objects.equals(id, localDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LocalDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", longitud='" + longitud + "'" +
            ", banner='" + banner + "'" +
            ", bannerUrl='" + bannerUrl + "'" +
            ", latitud='" + latitud + "'" +
            ", descripcion='" + descripcion + "'" +
            ", provincia='" + provincia + "'" +
            ", rating='" + rating + "'" +
            '}';
    }
}
