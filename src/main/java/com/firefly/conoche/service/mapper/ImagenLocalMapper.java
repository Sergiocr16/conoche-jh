package com.firefly.conoche.service.mapper;

import com.firefly.conoche.domain.*;
import com.firefly.conoche.service.dto.ImagenLocalDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ImagenLocal and its DTO ImagenLocalDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ImagenLocalMapper {

    @Mapping(source = "local.id", target = "localId")
    ImagenLocalDTO imagenLocalToImagenLocalDTO(ImagenLocal imagenLocal);

    List<ImagenLocalDTO> imagenLocalsToImagenLocalDTOs(List<ImagenLocal> imagenLocals);

    @Mapping(source = "localId", target = "local")
    ImagenLocal imagenLocalDTOToImagenLocal(ImagenLocalDTO imagenLocalDTO);

    List<ImagenLocal> imagenLocalDTOsToImagenLocals(List<ImagenLocalDTO> imagenLocalDTOs);

    default Local localFromId(Long id) {
        if (id == null) {
            return null;
        }
        Local local = new Local();
        local.setId(id);
        return local;
    }
}
