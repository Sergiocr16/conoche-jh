package com.firefly.conoche.repository.customRepository;

import com.firefly.conoche.domain.Event;

import com.firefly.conoche.domain.enumeration.Provincia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;


import java.util.List;

/**
 * Spring Data JPA repository for the Event entity.
 */
@SuppressWarnings("unused")
public interface CEventRepository extends JpaRepository<Event,Long> {
    Page<Event> findByLocalProvinciaAndNameContainingIgnoreCase(Pageable page, Provincia provincia, String name);
}
