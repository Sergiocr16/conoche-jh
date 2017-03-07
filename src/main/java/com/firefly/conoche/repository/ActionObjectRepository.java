package com.firefly.conoche.repository;

import com.firefly.conoche.domain.ActionObject;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ActionObject entity.
 */
@SuppressWarnings("unused")
public interface ActionObjectRepository extends JpaRepository<ActionObject,Long> {

}
