package com.firefly.conoche.repository;

import com.firefly.conoche.domain.EventImage;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EventImage entity.
 */
@SuppressWarnings("unused")
public interface EventImageRepository extends JpaRepository<EventImage,Long> {

}
