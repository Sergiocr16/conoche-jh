package com.firefly.conoche.service.mapper;

import com.firefly.conoche.domain.*;
import com.firefly.conoche.service.dto.EventImageDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity EventImage and its DTO EventImageDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EventImageMapper {

    @Mapping(source = "event.id", target = "eventId")
    EventImageDTO eventImageToEventImageDTO(EventImage eventImage);

    List<EventImageDTO> eventImagesToEventImageDTOs(List<EventImage> eventImages);

    @Mapping(source = "eventId", target = "event")
    EventImage eventImageDTOToEventImage(EventImageDTO eventImageDTO);

    List<EventImage> eventImageDTOsToEventImages(List<EventImageDTO> eventImageDTOs);

    default Event eventFromId(Long id) {
        if (id == null) {
            return null;
        }
        Event event = new Event();
        event.setId(id);
        return event;
    }
}
