package com.firefly.conoche.service.mapper;

import com.firefly.conoche.domain.*;
import com.firefly.conoche.service.dto.EventDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Event and its DTO EventDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, ServicioMapper.class, })
public interface EventMapper {

    @Mapping(source = "local.id", target = "localId")
    @Mapping(source = "local.name", target = "localName")
    EventDTO eventToEventDTO(Event event);

    List<EventDTO> eventsToEventDTOs(List<Event> events);

    @Mapping(target = "promotions", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "realTimeImages", ignore = true)
    @Mapping(source = "localId", target = "local")
    @Mapping(target = "messages", ignore = true)
    Event eventDTOToEvent(EventDTO eventDTO);

    List<Event> eventDTOsToEvents(List<EventDTO> eventDTOs);

    default Servicio servicioFromId(Long id) {
        if (id == null) {
            return null;
        }
        Servicio servicio = new Servicio();
        servicio.setId(id);
        return servicio;
    }

    default Local localFromId(Long id) {
        if (id == null) {
            return null;
        }
        Local local = new Local();
        local.setId(id);
        return local;
    }
}
