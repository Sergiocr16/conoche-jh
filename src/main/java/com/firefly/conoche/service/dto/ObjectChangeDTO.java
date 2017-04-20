package com.firefly.conoche.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ObjectChange entity.
 */
public class ObjectChangeDTO implements Serializable {

    private Long id;

    @NotNull
    private String fieldName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ObjectChangeDTO objectChangeDTO = (ObjectChangeDTO) o;

        if ( ! Objects.equals(id, objectChangeDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ObjectChangeDTO{" +
            "id=" + id +
            ", fieldName='" + fieldName + "'" +
            '}';
    }
}
