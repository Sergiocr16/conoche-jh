package com.firefly.conoche.repository;

import com.firefly.conoche.domain.Schedule;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Schedule entity.
 */
@SuppressWarnings("unused")
public interface ScheduleRepository extends JpaRepository<Schedule,Long> {

}
