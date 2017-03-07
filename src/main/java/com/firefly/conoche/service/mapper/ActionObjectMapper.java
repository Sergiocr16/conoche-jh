package com.firefly.conoche.service.mapper;

import com.firefly.conoche.domain.*;
import com.firefly.conoche.service.dto.ActionObjectDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ActionObject and its DTO ActionObjectDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ActionObjectMapper {

    @Mapping(source = "action.id", target = "actionId")
    ActionObjectDTO actionObjectToActionObjectDTO(ActionObject actionObject);

    List<ActionObjectDTO> actionObjectsToActionObjectDTOs(List<ActionObject> actionObjects);

    @Mapping(source = "actionId", target = "action")
    ActionObject actionObjectDTOToActionObject(ActionObjectDTO actionObjectDTO);

    List<ActionObject> actionObjectDTOsToActionObjects(List<ActionObjectDTO> actionObjectDTOs);

    default Action actionFromId(Long id) {
        if (id == null) {
            return null;
        }
        Action action = new Action();
        action.setId(id);
        return action;
    }
}
