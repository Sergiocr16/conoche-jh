package com.firefly.conoche.repository;

import com.firefly.conoche.domain.Event;


import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Event entity.
 */
@SuppressWarnings("unused")
public interface EventRepository extends JpaRepository<Event,Long> {

    @Query("select distinct event from Event event left join fetch event.attendingUsers left join fetch event.services")
    List<Event> findAllWithEagerRelationships();

    @Query("select event from Event event left join fetch event.attendingUsers left join fetch event.services where event.id =:id")
    Event findOneWithEagerRelationships(@Param("id") Long id);

}
