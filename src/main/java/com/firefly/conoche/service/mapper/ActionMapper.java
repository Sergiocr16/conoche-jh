package com.firefly.conoche.service.mapper;

import com.firefly.conoche.domain.*;
import com.firefly.conoche.service.dto.ActionDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Action and its DTO ActionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ActionMapper {

    ActionDTO actionToActionDTO(Action action);

    List<ActionDTO> actionsToActionDTOs(List<Action> actions);

    @Mapping(target = "objects", ignore = true)
    Action actionDTOToAction(ActionDTO actionDTO);

    List<Action> actionDTOsToActions(List<ActionDTO> actionDTOs);
}
