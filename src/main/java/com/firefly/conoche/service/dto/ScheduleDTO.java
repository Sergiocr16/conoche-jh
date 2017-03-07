package com.firefly.conoche.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.firefly.conoche.domain.enumeration.Day;
import com.firefly.conoche.domain.enumeration.Day;

/**
 * A DTO for the Schedule entity.
 */
public class ScheduleDTO implements Serializable {

    private Long id;

    @NotNull
    private Day initialDay;

    @NotNull
    private Day finalDay;

    @NotNull
    @Min(value = 0)
    @Max(value = 1439)
    private Integer initialTime;

    @NotNull
    @Min(value = 0)
    @Max(value = 1439)
    private Integer finalTime;

    private Long localId;

    private String localName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Day getInitialDay() {
        return initialDay;
    }

    public void setInitialDay(Day initialDay) {
        this.initialDay = initialDay;
    }
    public Day getFinalDay() {
        return finalDay;
    }

    public void setFinalDay(Day finalDay) {
        this.finalDay = finalDay;
    }
    public Integer getInitialTime() {
        return initialTime;
    }

    public void setInitialTime(Integer initialTime) {
        this.initialTime = initialTime;
    }
    public Integer getFinalTime() {
        return finalTime;
    }

    public void setFinalTime(Integer finalTime) {
        this.finalTime = finalTime;
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

        ScheduleDTO scheduleDTO = (ScheduleDTO) o;

        if ( ! Objects.equals(id, scheduleDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ScheduleDTO{" +
            "id=" + id +
            ", initialDay='" + initialDay + "'" +
            ", finalDay='" + finalDay + "'" +
            ", initialTime='" + initialTime + "'" +
            ", finalTime='" + finalTime + "'" +
            '}';
    }
}
