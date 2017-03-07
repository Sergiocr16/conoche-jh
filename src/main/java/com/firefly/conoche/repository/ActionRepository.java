package com.firefly.conoche.repository;

import com.firefly.conoche.domain.Action;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Action entity.
 */
@SuppressWarnings("unused")
public interface ActionRepository extends JpaRepository<Action,Long> {

    @Query("select distinct action from Action action left join fetch action.users")
    List<Action> findAllWithEagerRelationships();

    @Query("select action from Action action left join fetch action.users where action.id =:id")
    Action findOneWithEagerRelationships(@Param("id") Long id);

}
