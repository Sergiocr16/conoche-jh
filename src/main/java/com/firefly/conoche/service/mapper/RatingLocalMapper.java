package com.firefly.conoche.service.mapper;

import com.firefly.conoche.domain.*;
import com.firefly.conoche.service.dto.RatingLocalDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity RatingLocal and its DTO RatingLocalDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface RatingLocalMapper {

    @Mapping(source = "userDetails.id", target = "userDetailsId")
    @Mapping(source = "local.id", target = "localId")
    @Mapping(source = "local.name", target = "localName")
    RatingLocalDTO ratingLocalToRatingLocalDTO(RatingLocal ratingLocal);

    List<RatingLocalDTO> ratingLocalsToRatingLocalDTOs(List<RatingLocal> ratingLocals);

    @Mapping(source = "userDetailsId", target = "userDetails")
    @Mapping(source = "localId", target = "local")
    RatingLocal ratingLocalDTOToRatingLocal(RatingLocalDTO ratingLocalDTO);

    List<RatingLocal> ratingLocalDTOsToRatingLocals(List<RatingLocalDTO> ratingLocalDTOs);

    default Local localFromId(Long id) {
        if (id == null) {
            return null;
        }
        Local local = new Local();
        local.setId(id);
        return local;
    }
}
