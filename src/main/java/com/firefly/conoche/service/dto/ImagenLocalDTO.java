package com.firefly.conoche.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the ImagenLocal entity.
 */
public class ImagenLocalDTO implements Serializable {

    private Long id;

    private String imageUrl;

    @Lob
    private byte[] image;
    private String imageContentType;

    private Long localId;

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

    public Long getLocalId() {
        return localId;
    }

    public void setLocalId(Long localId) {
        this.localId = localId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ImagenLocalDTO imagenLocalDTO = (ImagenLocalDTO) o;

        if ( ! Objects.equals(id, imagenLocalDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ImagenLocalDTO{" +
            "id=" + id +
            ", imageUrl='" + imageUrl + "'" +
            ", image='" + image + "'" +
            '}';
    }
}
