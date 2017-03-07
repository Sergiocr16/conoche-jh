package com.firefly.conoche.repository;

import com.firefly.conoche.domain.RatingLocal;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RatingLocal entity.
 */
@SuppressWarnings("unused")
public interface RatingLocalRepository extends JpaRepository<RatingLocal,Long> {

    @Query("select ratingLocal from RatingLocal ratingLocal where ratingLocal.userDetails.login = ?#{principal.username}")
    List<RatingLocal> findByUserDetailsIsCurrentUser();

}
