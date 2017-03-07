package com.firefly.conoche.service.mapper;

import com.firefly.conoche.domain.*;
import com.firefly.conoche.service.dto.PromotionCodeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity PromotionCode and its DTO PromotionCodeDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface PromotionCodeMapper {

    @Mapping(source = "promotion.id", target = "promotionId")
    @Mapping(source = "user.id", target = "userId")
    PromotionCodeDTO promotionCodeToPromotionCodeDTO(PromotionCode promotionCode);

    List<PromotionCodeDTO> promotionCodesToPromotionCodeDTOs(List<PromotionCode> promotionCodes);

    @Mapping(source = "promotionId", target = "promotion")
    @Mapping(source = "userId", target = "user")
    PromotionCode promotionCodeDTOToPromotionCode(PromotionCodeDTO promotionCodeDTO);

    List<PromotionCode> promotionCodeDTOsToPromotionCodes(List<PromotionCodeDTO> promotionCodeDTOs);

    default Promotion promotionFromId(Long id) {
        if (id == null) {
            return null;
        }
        Promotion promotion = new Promotion();
        promotion.setId(id);
        return promotion;
    }
}
