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
    List<PromotionCode> findByPromotionIdAndUserIdIsNull(Long promotionId);
    List<PromotionCode> findByPromotionIdAndUserId(Long promotionId,Long userId);
    List<PromotionCode> findByUserId(Long userId);
    PromotionCode findTop1ByPromotionIdAndUserIdIsNull(Long promotionId);
}
