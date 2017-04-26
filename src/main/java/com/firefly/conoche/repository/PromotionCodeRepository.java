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
    List<PromotionCode> findByPromotionIdAndUserIdIsNullAndActive(Long promotionId,boolean active);
    List<PromotionCode> findByPromotionIdAndUserId(Long promotionId,Long userId);
    List<PromotionCode> findByUserIdAndActive(Long userId,boolean active);
    PromotionCode findTop1ByPromotionIdAndUserIdIsNull(Long promotionId);
    PromotionCode findByCodeAndActive(String code,boolean active);
}
