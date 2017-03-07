package com.firefly.conoche.repository;

import com.firefly.conoche.domain.PromotionCode;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PromotionCode entity.
 */
@SuppressWarnings("unused")
public interface PromotionCodeRepository extends JpaRepository<PromotionCode,Long> {

    @Query("select promotionCode from PromotionCode promotionCode where promotionCode.user.login = ?#{principal.username}")
    List<PromotionCode> findByUserIsCurrentUser();

}
