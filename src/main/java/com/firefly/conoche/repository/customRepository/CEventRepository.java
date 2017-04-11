package com.firefly.conoche.repository.customRepository;

import com.firefly.conoche.domain.Event;

import com.firefly.conoche.domain.enumeration.Provincia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the Event entity.
 */
@SuppressWarnings("unused")
public interface CEventRepository extends JpaRepository<Event,Long> {
    Page<Event> findByLocalProvinciaAndNameContainingIgnoreCase(Pageable page, Provincia provincia, String name);

    @Query("select event from Event event where " +
        "(:prov is null or event.local.provincia = :prov) " +
        "and lower(event.name) like lower(concat('%', :name,'%')) " +
        "and (:date is null or event.finalTime > :date)")
    Page<Event> findByProvincia(Pageable page,
                                @Param("prov") Provincia provincia,
                                @Param("name") String name,
                                @Param("date") ZonedDateTime zonedDateTime);

    Long countByFinalTimeGreaterThan(ZonedDateTime zonedDateTime);
}
