package com.firefly.conoche.repository;

import com.firefly.conoche.domain.RealTimeEventImage;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RealTimeEventImage entity.
 */
@SuppressWarnings("unused")
public interface RealTimeEventImageRepository extends JpaRepository<RealTimeEventImage,Long> {

}
