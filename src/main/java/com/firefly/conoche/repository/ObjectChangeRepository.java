package com.firefly.conoche.repository;

import com.firefly.conoche.domain.ObjectChange;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ObjectChange entity.
 */
@SuppressWarnings("unused")
public interface ObjectChangeRepository extends JpaRepository<ObjectChange,Long> {

}
