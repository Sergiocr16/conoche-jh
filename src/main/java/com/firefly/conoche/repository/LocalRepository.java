package com.firefly.conoche.repository;

import com.firefly.conoche.domain.Local;

import com.firefly.conoche.repository.notifications.NotifyRepository;
import com.firefly.conoche.repository.notifications.especificImpl.LocalRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Local entity.
 * Mover findBylocalCategoryId a otro servicio para evitar perderlos
 */
@SuppressWarnings("unused")
public interface LocalRepository extends NotifyRepository<Local>, LocalRepositoryCustom {

    @Query("select local from Local local where local.owner.login = ?#{principal.username}")
    List<Local> findByOwnerIsCurrentUser();

    @Query("select distinct local from Local local left join fetch local.services left join fetch local.subcribers")
    List<Local> findAllWithEagerRelationships();
    List<Local> findByOwnerId(Long ownerId);
    Page<Local> findBylocalCategoryId(Long categoryId, Pageable pageable);

    @Query("select local from Local local left join fetch local.services left join fetch local.subcribers where local.id =:id")
    Local findOneWithEagerRelationships(@Param("id") Long id);

}
