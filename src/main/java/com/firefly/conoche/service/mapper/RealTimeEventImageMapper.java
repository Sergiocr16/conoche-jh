package com.firefly.conoche.service.mapper;

import com.firefly.conoche.domain.*;
import com.firefly.conoche.service.dto.RealTimeEventImageDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity RealTimeEventImage and its DTO RealTimeEventImageDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RealTimeEventImageMapper {

    @Mapping(source = "event.id", target = "eventId")
    RealTimeEventImageDTO realTimeEventImageToRealTimeEventImageDTO(RealTimeEventImage realTimeEventImage);

    List<RealTimeEventImageDTO> realTimeEventImagesToRealTimeEventImageDTOs(List<RealTimeEventImage> realTimeEventImages);

    @Mapping(source = "eventId", target = "event")
    RealTimeEventImage realTimeEventImageDTOToRealTimeEventImage(RealTimeEventImageDTO realTimeEventImageDTO);

    List<RealTimeEventImage> realTimeEventImageDTOsToRealTimeEventImages(List<RealTimeEventImageDTO> realTimeEventImageDTOs);

    default Event eventFromId(Long id) {
        if (id == null) {
            return null;
        }
        Event event = new Event();
        event.setId(id);
        return event;
    }
}
