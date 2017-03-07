package com.firefly.conoche.service.mapper;

import com.firefly.conoche.domain.*;
import com.firefly.conoche.service.dto.PromotionDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Promotion and its DTO PromotionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PromotionMapper {

    @Mapping(source = "event.id", target = "eventId")
    PromotionDTO promotionToPromotionDTO(Promotion promotion);

    List<PromotionDTO> promotionsToPromotionDTOs(List<Promotion> promotions);

    @Mapping(target = "codes", ignore = true)
    @Mapping(source = "eventId", target = "event")
    Promotion promotionDTOToPromotion(PromotionDTO promotionDTO);

    List<Promotion> promotionDTOsToPromotions(List<PromotionDTO> promotionDTOs);

    default Event eventFromId(Long id) {
        if (id == null) {
            return null;
        }
        Event event = new Event();
        event.setId(id);
        return event;
    }
}
