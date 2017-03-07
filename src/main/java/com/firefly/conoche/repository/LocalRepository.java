package com.firefly.conoche.repository;

import com.firefly.conoche.domain.Local;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Local entity.
 */
@SuppressWarnings("unused")
public interface LocalRepository extends JpaRepository<Local,Long> {

    @Query("select distinct local from Local local left join fetch local.services left join fetch local.subcribers")
    List<Local> findAllWithEagerRelationships();

    @Query("select local from Local local left join fetch local.services left join fetch local.subcribers where local.id =:id")
    Local findOneWithEagerRelationships(@Param("id") Long id);

}
