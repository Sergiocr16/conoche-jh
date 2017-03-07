package com.firefly.conoche.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.firefly.conoche.domain.enumeration.Day;

/**
 * A Schedule.
 */
@Entity
@Table(name = "schedule")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Schedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "initial_day", nullable = false)
    private Day initialDay;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "final_day", nullable = false)
    private Day finalDay;

    @NotNull
    @Min(value = 0)
    @Max(value = 1439)
    @Column(name = "initial_time", nullable = false)
    private Integer initialTime;

    @NotNull
    @Min(value = 0)
    @Max(value = 1439)
    @Column(name = "final_time", nullable = false)
    private Integer finalTime;

    @ManyToOne
    private Local local;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Day getInitialDay() {
        return initialDay;
    }

    public Schedule initialDay(Day initialDay) {
        this.initialDay = initialDay;
        return this;
    }

    public void setInitialDay(Day initialDay) {
        this.initialDay = initialDay;
    }

    public Day getFinalDay() {
        return finalDay;
    }

    public Schedule finalDay(Day finalDay) {
        this.finalDay = finalDay;
        return this;
    }

    public void setFinalDay(Day finalDay) {
        this.finalDay = finalDay;
    }

    public Integer getInitialTime() {
        return initialTime;
    }

    public Schedule initialTime(Integer initialTime) {
        this.initialTime = initialTime;
        return this;
    }

    public void setInitialTime(Integer initialTime) {
        this.initialTime = initialTime;
    }

    public Integer getFinalTime() {
        return finalTime;
    }

    public Schedule finalTime(Integer finalTime) {
        this.finalTime = finalTime;
        return this;
    }

    public void setFinalTime(Integer finalTime) {
        this.finalTime = finalTime;
    }

    public Local getLocal() {
        return local;
    }

    public Schedule local(Local local) {
        this.local = local;
        return this;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Schedule schedule = (Schedule) o;
        if (schedule.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, schedule.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Schedule{" +
            "id=" + id +
            ", initialDay='" + initialDay + "'" +
            ", finalDay='" + finalDay + "'" +
            ", initialTime='" + initialTime + "'" +
            ", finalTime='" + finalTime + "'" +
            '}';
    }
}
