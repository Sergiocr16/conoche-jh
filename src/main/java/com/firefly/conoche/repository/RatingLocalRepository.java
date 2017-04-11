package com.firefly.conoche.repository;

import com.firefly.conoche.domain.RatingLocal;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the RatingLocal entity.
 */
@SuppressWarnings("unused")
public interface RatingLocalRepository extends JpaRepository<RatingLocal,Long> {

    @Query("select ratingLocal from RatingLocal ratingLocal where ratingLocal.userDetails.login = ?#{principal.username}")
    List<RatingLocal> findByUserDetailsIsCurrentUser();

    RatingLocal findByUserLoginAndLocalId(String login, Long localId);

//    @Query("select avg(rl.rating) from RatingLocal rl " +
//           "where rl.local.id = ?1")
//    double calculateRating(Long localId);

    List<RatingLocal> findByLocalId(Long localId);


}
