package com.firefly.conoche.service.mapper;

import com.firefly.conoche.domain.*;
import com.firefly.conoche.service.dto.ActionObjectDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ActionObject and its DTO ActionObjectDTO.
 */
@Mapper(componentModel = "spring", uses = {ObjectChangeMapper.class,})
public interface ActionObjectMapper {

    ActionObjectDTO actionObjectToActionObjectDTO(ActionObject actionObject);

    List<ActionObjectDTO> actionObjectsToActionObjectDTOs(List<ActionObject> actionObjects);


    ActionObject actionObjectDTOToActionObject(ActionObjectDTO actionObjectDTO);

    List<ActionObject> actionObjectDTOsToActionObjects(List<ActionObjectDTO> actionObjectDTOs);
}
