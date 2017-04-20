package com.firefly.conoche.service.mapper;

import com.firefly.conoche.domain.*;
import com.firefly.conoche.service.dto.ObjectChangeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ObjectChange and its DTO ObjectChangeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ObjectChangeMapper {

    ObjectChangeDTO objectChangeToObjectChangeDTO(ObjectChange objectChange);

    List<ObjectChangeDTO> objectChangesToObjectChangeDTOs(List<ObjectChange> objectChanges);

    ObjectChange objectChangeDTOToObjectChange(ObjectChangeDTO objectChangeDTO);

    List<ObjectChange> objectChangeDTOsToObjectChanges(List<ObjectChangeDTO> objectChangeDTOs);
}
