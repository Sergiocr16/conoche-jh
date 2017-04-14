package com.firefly.conoche.repository;

import com.firefly.conoche.domain.Promotion;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Promotion entity.
 */
@SuppressWarnings("unused")
public interface PromotionRepository extends JpaRepository<Promotion,Long> {
    List<Promotion> findByEventId(Long eventId);
}
